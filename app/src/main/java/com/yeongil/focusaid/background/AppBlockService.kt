package com.yeongil.focusaid.background

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Intent
import android.os.SystemClock
import android.view.accessibility.AccessibilityEvent
import com.yeongil.focusaid.AppBlockActivity
import com.yeongil.focusaid.data.BlockingApp
import com.yeongil.focusaid.data.BlockingApp.BlockingAppActionType
import com.yeongil.focusaid.data.rule.action.AppBlockAction
import com.yeongil.focusaid.dataSource.blockingAppDatabase.BlockingAppDatabase
import com.yeongil.focusaid.repository.BlockingAppRepository
import com.yeongil.focusaid.repository.PackageManagerRepository
import com.yeongil.focusaid.utils.ALERT
import com.yeongil.focusaid.utils.CLOSE_IMMEDIATE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AppBlockService : AccessibilityService() {
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private val blockingAppRepo by lazy {
        BlockingAppRepository(BlockingAppDatabase.getInstance(this).blockingAppDao())
    }
    private val packageManagerRepo by lazy { PackageManagerRepository(packageManager) }
    private val usageStatsManager by lazy { getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager }
    private val alarmManager by lazy { getSystemService(ALARM_SERVICE) as AlarmManager }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent == null) return START_STICKY

        when (intent.action) {
            /* Check usage time of specific application */
            CHECK_APP_USAGE -> {
                val packageName = intent.getStringExtra(PACKAGE_NAME_EXTRA_KEY)
                    ?: return START_STICKY
                serviceScope.launch { checkUsageTime(packageName) }
            }
            /* Apply new AppBlockAction */
            SUBMIT_APP_BLOCK_ACTION -> {
                val action = intent.getParcelableExtra<AppBlockAction?>(APP_BLOCK_EXTRA_KEY)
                val rid = intent.getIntExtra(RID_EXTRA_KEY, 0)
                serviceScope.launch { applyAppBlockAction(rid, action) }
            }
            /* Extend allowed time */
            EXTEND_ALLOWED_TIME -> {
                val packageName = intent.getStringExtra(PACKAGE_NAME_EXTRA_KEY)
                    ?: return START_STICKY
                val extraSeconds = intent.getIntExtra(EXTRA_SECONDS_EXTRA_KEY, 60)

                serviceScope.launch {
                    val blockingApp = blockingAppRepo.getBlockingAppByPackageName(packageName)
                        ?: return@launch

                    blockingAppRepo.updateBlockingApp(
                        blockingApp.copy(
                            timestamp = System.currentTimeMillis(),
                            allowedTimeInSeconds = extraSeconds,
                            allowedForThisExecution = false,
                        )
                    )

                    setAlarm(packageName, extraSeconds.toLong() * 1000)
                }
            }
            /* Remove the package from blocking apps */
            ALLOW_FOR_THIS_EXECUTION -> {
                val packageName = intent.getStringExtra(PACKAGE_NAME_EXTRA_KEY)
                    ?: return START_STICKY

                serviceScope.launch {
                    val blockingApp = blockingAppRepo.getBlockingAppByPackageName(packageName)
                        ?: return@launch

                    blockingAppRepo.updateBlockingApp(
                        blockingApp.copy(
                            timestamp = System.currentTimeMillis(),
                            allowedTimeInSeconds = 0,
                            allowedForThisExecution = true
                        )
                    )
                }
            }
        }

        return START_STICKY
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val packageName = event?.packageName?.toString() ?: return
        if (packageManagerRepo.isSystemApp(packageName)) return
        serviceScope.launch(Dispatchers.Default) { checkUsageTime(packageName) }
    }

    override fun onInterrupt() {}

    override fun onServiceConnected() {
        /* Initialize settings for Accessibility Service */
        serviceInfo.apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK
        }
    }

    override fun onDestroy() {
        /* Cancel all coroutine jobs when the service is destroyed */
        super.onDestroy()
        serviceJob.cancel()
    }

    private suspend fun applyAppBlockAction(rid: Int, action: AppBlockAction?) {
        blockingAppRepo.deleteAllBlockingApps()
        if (action == null || rid == 0) return

        if (action.allAppBlock) {
            blockingAppRepo.insertBlockingApp(
                BlockingApp(
                    rid,
                    BlockingApp.ALL_APP,
                    System.currentTimeMillis(),
                    0,
                    false,
                    when (action.allAppHandlingAction) {
                        CLOSE_IMMEDIATE -> BlockingAppActionType.CLOSE
                        ALERT -> BlockingAppActionType.ALERT
                        else -> throw Exception("Invalid all app handling action")
                    }
                )
            )
        } else {
            blockingAppRepo.insertBlockingApps(
                action.appBlockEntryList.map {
                    BlockingApp(
                        rid,
                        it.packageName,
                        System.currentTimeMillis(),
                        it.allowedTimeInMinutes * 60,
                        false,
                        when (it.handlingAction) {
                            CLOSE_IMMEDIATE -> BlockingAppActionType.CLOSE
                            ALERT -> BlockingAppActionType.ALERT
                            else -> throw Exception("Invalid all app handling action")
                        }
                    )
                })
        }
    }

    /* Check usage time of the package. If the usage time exceeds the allowed time, block app. */
    private suspend fun checkUsageTime(packageName: String) {
        if (packageName == this.packageName) return

        val blockingApp = blockingAppRepo.getBlockingAppByPackageName(BlockingApp.ALL_APP)
            ?: blockingAppRepo.getBlockingAppByPackageName(packageName)
            ?: return
        val isAllApp = blockingApp.packageName == BlockingApp.ALL_APP

        /* Check usage time */
        val from = blockingApp.timestamp
        val allowedTime = blockingApp.allowedTimeInSeconds
        val allowedForThisExecution = blockingApp.allowedForThisExecution
        val (usageTime, lastEventType, interrupted, lastPackageName) =
            getUsageStatus(blockingApp.packageName, from, allowedTime)

        if (usageTime / 1000 >= blockingApp.allowedTimeInSeconds) {
            /* Check if this execution was allowed. Otherwise,
            * Block Application if the usage time exceeds allowed time. */
            if (!allowedForThisExecution || interrupted) {
                if (isAllApp && lastPackageName != null) {
                    blockApp(blockingApp, lastPackageName)
                } else if (!isAllApp) {
                    blockApp(blockingApp, packageName)
                }
            }
        } else if (lastEventType == UsageEvents.Event.ACTIVITY_RESUMED) {
            /* If the user is currently using this application,
             * check again after the allowed time has elapsed */
            setAlarm(
                if (isAllApp) BlockingApp.ALL_APP else packageName,
                blockingApp.allowedTimeInSeconds * 1000 - usageTime
            )
        }
    }

    private fun getUsageStatus(
        packageName: String,
        from: Long,
        limit: Int
    ): UsageStatus {
        val events = usageStatsManager.queryEvents(from, System.currentTimeMillis())

        var offset = from
        var usageTime: Long = 0
        var lastEventType = UsageEvents.Event.ACTIVITY_PAUSED
        var interrupted = false
        var lastPackageName: String? = null

        while (events.hasNextEvent()) {
            val appEvent = UsageEvents.Event()
            events.getNextEvent(appEvent)

            /* Ignore other packages */
            if (packageName == BlockingApp.ALL_APP && packageManagerRepo.isSystemApp(appEvent.packageName)) continue
            if (packageName != BlockingApp.ALL_APP && packageName != appEvent.packageName) continue

            /* Calculate usage time */
            when (appEvent.eventType) {
                UsageEvents.Event.ACTIVITY_PAUSED -> {
                    if (offset != 0L) {
                        usageTime += (appEvent.timeStamp - offset)
                        offset = 0
                        interrupted = true

                        if (usageTime / 1000 >= limit) break
                    }
                    lastEventType = UsageEvents.Event.ACTIVITY_PAUSED
                    lastPackageName = null
                }
                UsageEvents.Event.ACTIVITY_RESUMED -> {
                    if (appEvent.timeStamp > offset) offset = appEvent.timeStamp
                    lastEventType = UsageEvents.Event.ACTIVITY_RESUMED
                    lastPackageName = appEvent.packageName
                }
            }
        }
        if (offset != 0L) usageTime += System.currentTimeMillis() - offset

        return UsageStatus(usageTime, lastEventType, interrupted, lastPackageName)
    }

    /* Block app by showing a dialog window. */
    private fun blockApp(blockingApp: BlockingApp, blockingPackageName: String) {
        val actionType = when (blockingApp.action) {
            BlockingAppActionType.CLOSE -> AppBlockActivity.CLOSE_IMMEDIATE
            BlockingAppActionType.ALERT -> AppBlockActivity.ALERT
        }

        val intent = Intent(this, AppBlockActivity::class.java).apply {
            action = actionType

            // An app that is actually blocked */
            putExtra(AppBlockActivity.PACKAGE_NAME_KEY, blockingPackageName)
            // Primary key of app block database (for the ALL_APP case)
            putExtra(AppBlockActivity.BLOCKING_APP_KEY_KEY, blockingApp.packageName)

            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                        or Intent.FLAG_ACTIVITY_NO_HISTORY
            )
        }

        startActivity(intent)
    }

    /* Wake up this service after TIME_IN_MILLIS ms and check usage time of PACKAGE_NAME app */
    private fun setAlarm(packageName: String, timeInMillis: Long) {
        val pendingIntent = Intent(this, AppBlockService::class.java)
            .apply {
                action = CHECK_APP_USAGE
                putExtra(PACKAGE_NAME_EXTRA_KEY, packageName)
            }
            .let { PendingIntent.getService(this, 0, it, PendingIntent.FLAG_IMMUTABLE) }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + timeInMillis,
            pendingIntent
        )
    }

    private data class UsageStatus(
        val usageTime: Long = 0,
        val lastEventType: Int = UsageEvents.Event.ACTIVITY_PAUSED,
        val interrupted: Boolean = false,
        val lastPackageName: String? = null,
    )

    companion object {
        const val APP_BLOCK_EXTRA_KEY = "APP_BLOCK_EXTRA"
        const val RID_EXTRA_KEY = "RID"
        const val PACKAGE_NAME_EXTRA_KEY = "PACKAGE_NAME"
        const val EXTRA_SECONDS_EXTRA_KEY = "EXTRA_TIME"

        const val CHECK_APP_USAGE = "CHECK_APP_USAGE"
        const val SUBMIT_APP_BLOCK_ACTION = "SUBMIT_APP_BLOCK_ACTION"
        const val EXTEND_ALLOWED_TIME = "EXTEND_ALLOWED_TIME"
        const val ALLOW_FOR_THIS_EXECUTION = "ALLOW_FOR_THIS_EXECUTION"
    }
}