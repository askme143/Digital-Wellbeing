package com.yeongil.digitalwellbeing.background

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.yeongil.digitalwellbeing.data.BlockingApp
import com.yeongil.digitalwellbeing.data.BlockingApp.BlockingAppActionType
import com.yeongil.digitalwellbeing.data.rule.action.AppBlockAction
import com.yeongil.digitalwellbeing.dataSource.blockingAppDatabase.BlockingAppDatabase
import com.yeongil.digitalwellbeing.repository.BlockingAppRepository
import com.yeongil.digitalwellbeing.utils.ALERT
import com.yeongil.digitalwellbeing.utils.CLOSE_IMMEDIATE
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
    private val usageStatsManager by lazy { getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager }
    private val alarmManager by lazy { getSystemService(ALARM_SERVICE) as AlarmManager }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent == null) return START_STICKY

        when (intent.action) {
            CHECK_PACKAGE -> {
                val packageName = intent.getStringExtra(PACKAGE_NAME_EXTRA_KEY)
                    ?: return START_STICKY
                serviceScope.launch { checkUsageTime(packageName) }
            }
            SUBMIT_APP_BLOCK_ACTION -> {
                val action = intent.getParcelableExtra<AppBlockAction?>(APP_BLOCK_EXTRA_KEY)
                val rid = intent.getIntExtra(RID_EXTRA_KEY, 0)
                serviceScope.launch { applyAppBlockAction(rid, action) }
            }
        }

        return START_STICKY
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val packageName = event?.packageName?.toString() ?: return

        serviceScope.launch(Dispatchers.Default) { checkUsageTime(packageName) }
    }

    override fun onInterrupt() {}

    override fun onServiceConnected() {
        serviceInfo.apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }

    private suspend fun applyAppBlockAction(rid: Int, action: AppBlockAction?) {
        if (action == null || rid == 0)
            blockingAppRepo.deleteAllBlockingApps()
        else {
            if (action.allAppBlock) {
                blockingAppRepo.insertBlockingApp(
                    BlockingApp(
                        rid,
                        BlockingApp.ALL_APP, System.currentTimeMillis(), 0,
                        when (action.allAppHandlingAction) {
                            CLOSE_IMMEDIATE -> BlockingAppActionType.CLOSE
                            ALERT -> BlockingAppActionType.ALERT
                            else -> throw Exception("Invalid all app handling action")
                        }
                    )
                )
            } else {
                action.appBlockEntryList.map {
                    BlockingApp(
                        rid,
                        it.packageName,
                        System.currentTimeMillis(),
                        it.allowedTimeInMinutes,
                        when (it.handlingAction) {
                            CLOSE_IMMEDIATE -> BlockingAppActionType.CLOSE
                            ALERT -> BlockingAppActionType.ALERT
                            else -> throw Exception("Invalid all app handling action")
                        }
                    )
                }.let { blockingAppRepo.insertBlockingApps(it) }
            }
        }
    }

    private suspend fun checkUsageTime(packageName: String) {
        /* TODO: Test */
        val blockingApp = blockingAppRepo.getBlockingAppByPackageName(packageName)
        val allAppBlocking = blockingAppRepo.getBlockingAppByPackageName(BlockingApp.ALL_APP)

        if (allAppBlocking != null) {
            startHandling(allAppBlocking)
            return
        }
        if (blockingApp == null) return

        Log.e("hello_check_usage", "hello")

        /* Check usage time */
        val events =
            usageStatsManager.queryEvents(blockingApp.timestamp, System.currentTimeMillis())
        var offset = blockingApp.timestamp
        var usageTime: Long = 0
        var lastEventType = UsageEvents.Event.ACTIVITY_PAUSED
        while (events.hasNextEvent()) {
            val appEvent = UsageEvents.Event()
            events.getNextEvent(appEvent)

            /* Ignore other packages */
            if (appEvent.packageName != packageName) continue

            /* Calculate usage time */
            when (appEvent.eventType) {
                UsageEvents.Event.ACTIVITY_PAUSED -> {
                    if (offset != 0L) {
                        usageTime += (appEvent.timeStamp - offset)
                        offset = 0

                        if (usageTime / 1000 / 60 >= blockingApp.allowedTimeInMinutes) break
                    }
                    lastEventType = UsageEvents.Event.ACTIVITY_PAUSED
                }
                UsageEvents.Event.ACTIVITY_RESUMED -> {
                    if (appEvent.timeStamp > offset) offset = appEvent.timeStamp
                    lastEventType = UsageEvents.Event.ACTIVITY_RESUMED
                }
            }
        }
        if (offset != 0L) usageTime += System.currentTimeMillis() - offset

        if (usageTime / 1000 / 60 >= blockingApp.allowedTimeInMinutes)
            startHandling(blockingApp)
        else if (lastEventType == UsageEvents.Event.ACTIVITY_RESUMED) {
            Log.e("hello_check_usage", "set alarm")
            val pendingIntent = Intent(this, AppBlockService::class.java)
                .apply {
                    action = CHECK_PACKAGE
                    putExtra(PACKAGE_NAME_EXTRA_KEY, packageName)
                }
                .let { PendingIntent.getService(this, 0, it, 0) }

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + blockingApp.allowedTimeInMinutes * 60 * 1000 - usageTime,
                pendingIntent
            )
        }
    }

    private fun startHandling(blockingApp: BlockingApp) {
        when (blockingApp.action) {
            BlockingAppActionType.CLOSE -> {
                /* TODO: Show dialog */
                goHome()
            }
            BlockingAppActionType.ALERT -> {
                /* TODO: Show dialog */
                /* TODO: Alert */
            }
        }
    }

    private fun goHome() {
        val intent = Intent()
        intent.action = "android.intent.action.MAIN"
        intent.addCategory("android.intent.category.HOME")
        intent.addFlags(
            Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                    or Intent.FLAG_ACTIVITY_FORWARD_RESULT
                    or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
                    or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        )
        startActivity(intent)
    }

    companion object {
        const val APP_BLOCK_EXTRA_KEY = "APP_BLOCK_EXTRA"
        const val RID_EXTRA_KEY = "RID"
        const val PACKAGE_NAME_EXTRA_KEY = "PACKAGE_NAME"

        const val CHECK_PACKAGE = "CHECK_PACKAGE"
        const val SUBMIT_APP_BLOCK_ACTION = "SUBMIT_APP_BLOCK_ACTION"
    }
}