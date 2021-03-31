package com.yeongil.focusaid.background

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Build
import android.os.Parcelable
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.edit
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.yeongil.focusaid.MainActivity
import com.yeongil.focusaid.R
import com.yeongil.focusaid.background.data.RuleSet
import com.yeongil.focusaid.data.rule.Rule
import com.yeongil.focusaid.data.rule.action.*
import com.yeongil.focusaid.data.rule.action.RingerAction.RingerMode
import com.yeongil.focusaid.dataSource.SequenceNumber
import com.yeongil.focusaid.dataSource.ruleDatabase.RuleDatabase
import com.yeongil.focusaid.repository.RuleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainService : LifecycleService() {
    private val sharedPref by lazy { getSharedPreferences(MAIN_SERVICE_PREF_NAME, MODE_PRIVATE) }
    private val ruleRepo by lazy {
        RuleRepository(SequenceNumber(this), RuleDatabase.getInstance(this).ruleDao())
    }
    private val builder by lazy {
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText("실행 중")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(
                TaskStackBuilder.create(this).run {
                    addNextIntentWithParentStack(
                        Intent(this@MainService, MainActivity::class.java)
                    )
                    getPendingIntent(0, 0)
                }
            )
            .setOngoing(true)
    }
    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        when (intent?.action) {
            TIME_TRIGGER -> {
                Log.e("hello", "TIME_TRIGGER")
                val intentStr = intent.getStringExtra(TIME_TRIGGERED_RULES_KEY) ?: emptySetStr
                val ruleIdSet = Json.decodeFromString<Set<Int>>(intentStr)

                updateTimeTriggeredRules(intentStr)
                lifecycleScope.launch(Dispatchers.Default) {
                    checkTrigger(timeTriggerSet = ruleIdSet)
                }
            }
            ACTIVITY_TRIGGER -> {
                Log.e("hello", "ACTIVITY_TRIGGER")
                val intentStr = intent.getStringExtra(ACTIVITY_TRIGGERED_RULES_KEY) ?: emptySetStr
                val ruleIdSet = Json.decodeFromString<Set<Int>>(intentStr)

                updateActivityTriggeredRules(intentStr)
                lifecycleScope.launch(Dispatchers.Default) {
                    checkTrigger(activityTriggerSet = ruleIdSet)
                }
            }
            LOCATION_TRIGGER -> {
                Log.e("hello", "LOCATION_TRIGGER")
                val intentStr = intent.getStringExtra(LOCATION_TRIGGERED_RULES_KEY) ?: emptySetStr
                val ruleIdSet = Json.decodeFromString<Set<Int>>(intentStr)

                updateLocationTriggeredRules(intentStr)
                lifecycleScope.launch(Dispatchers.Default) {
                    checkTrigger(locationTriggerSet = ruleIdSet)
                }
            }
            START_BACKGROUND -> {
                Log.e("hello", "START_BACKGROUND")
                reset()
                startTriggerObserving()
            }
            RULE_CHANGE -> {
                Log.e("hello", "RULE_CHANGE")
                startTriggerObserving()
            }
            RULE_EXEC_CONFIRM -> {
                Log.e("hello", "RULE_EXEC_CONFIRM")
                val ruleId = intent.getIntExtra(CONFIRMED_RULE_ID_KEY, 0)
                lifecycleScope.launch(Dispatchers.Default) {
                    if (ruleId != 0) confirmRuleExec(ruleId)
                }
            }
        }

        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create Notification Channel for ForegroundService */
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "FocusAid Channel",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "FocusAid Background"
                setShowBadge(false)
            }
            notificationManager.createNotificationChannel(serviceChannel)

            /* Create Notification Channel for Notified Rules */
            val notifyChannel = NotificationChannel(
                RULE_NOTIFICATION_CHANNEL_ID,
                "FocusAid Rule Confirm Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "FocusAid Rule Execution Confirm"
            }

            notificationManager.createNotificationChannel(notifyChannel)
        }

        /* Listen for the ringer mode change */
        val ringerChangeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == AudioManager.RINGER_MODE_CHANGED_ACTION) {
                    val ringerIntent = Intent(context, RingerService::class.java).apply {
                        action = RingerService.RINGER_MODE_CHANGE_DETECTED
                    }
                    startService(ringerIntent)
                }
            }
        }
        val filter = IntentFilter().apply { addAction(AudioManager.RINGER_MODE_CHANGED_ACTION) }
        registerReceiver(ringerChangeReceiver, filter)

        startForeground(1, builder.build())
    }

    private fun getTimeTriggeredRules(): Set<Int> {
        return Json.decodeFromString(
            sharedPref.getString(TIME_TRIGGERED_RULES_KEY, emptySetStr) ?: emptySetStr
        )
    }

    private fun getActivityTriggeredRules(): Set<Int> {
        return Json.decodeFromString(
            sharedPref.getString(ACTIVITY_TRIGGERED_RULES_KEY, emptySetStr) ?: emptySetStr
        )
    }

    private fun getLocationTriggeredRules(): Set<Int> {
        return Json.decodeFromString(
            sharedPref.getString(LOCATION_TRIGGERED_RULES_KEY, emptySetStr) ?: emptySetStr
        )
    }

    private fun updateTimeTriggeredRules(str: String) {
        sharedPref.edit {
            putString(TIME_TRIGGERED_RULES_KEY, str)
            putLong(TIMESTAMP_TIME_TRIGGER_KEY, System.currentTimeMillis())
            commit()
        }
    }

    private fun updateActivityTriggeredRules(str: String) {
        sharedPref.edit {
            putString(ACTIVITY_TRIGGERED_RULES_KEY, str)
            putLong(TIMESTAMP_ACTIVITY_TRIGGER_KEY, System.currentTimeMillis())
            commit()
        }
    }

    private fun updateLocationTriggeredRules(str: String) {
        sharedPref.edit {
            putString(LOCATION_TRIGGERED_RULES_KEY, str)
            putLong(TIMESTAMP_LOCATION_TRIGGER_KEY, System.currentTimeMillis())
            commit()
        }
    }

    private fun getRuleSet(): RuleSet {
        return Json.decodeFromString(
            sharedPref.getString(RULE_SET_KEY, ruleSetDefaultStr) ?: ruleSetDefaultStr
        )
    }

    private fun updateRuleSet(ruleSet: RuleSet) {
        sharedPref.edit {
            putString(RULE_SET_KEY, Json.encodeToString(ruleSet))
            putLong(TIMESTAMP_RULE_SET_KEY, System.currentTimeMillis())
            commit()
        }
    }

    private fun reset() {
        updateLocationTriggeredRules(emptySetStr)
        updateActivityTriggeredRules(emptySetStr)
        updateTimeTriggeredRules(emptySetStr)
        updateRuleSet(RuleSet())
    }

    private suspend fun checkTrigger(
        timeTriggerSet: Set<Int> = getTimeTriggeredRules(),
        activityTriggerSet: Set<Int> = getActivityTriggeredRules(),
        locationTriggerSet: Set<Int> = getLocationTriggeredRules(),
    ) {
        val rules = ruleRepo.getActiveRuleList()
        val ruleSet = getRuleSet()

        val triggeredRules = rules
            .filter { it.timeTrigger == null || it.ruleInfo.ruleId in timeTriggerSet }
            .filter { it.activityTrigger == null || it.ruleInfo.ruleId in activityTriggerSet }
            .filter { it.locationTrigger == null || it.ruleInfo.ruleId in locationTriggerSet }

        val rulesToNotify = triggeredRules
            .filter { it.ruleInfo.notiOnTrigger }
            .minus(ruleSet.conflicting)
            .minus(ruleSet.running)
        val rulesToRun =
            handleConflict(
                triggeredRules - rulesToNotify,
                ruleSet.conflicting,
                ruleSet.running
            )
        val rulesToBeConflicted = triggeredRules - rulesToNotify - rulesToRun

        val newRuleSet = RuleSet(rulesToNotify, rulesToBeConflicted, rulesToRun)
        updateRuleSet(newRuleSet)

        /* update main notification */
        builder.setContentText(
            when (rulesToRun.size) {
                0 -> "수행 중인 규칙이 없습니다."
                1 -> "현재 [${rulesToRun[0].ruleInfo.ruleName}] 규칙이 수행 중입니다."
                else -> "현재 [${rulesToRun[0].ruleInfo.ruleName}]외 ${rulesToRun.size - 1}개 규칙이 수행 중입니다."
            }
        )
        builder.setWhen(System.currentTimeMillis())
        notificationManager.notify(1, builder.build())

        /* make notification */
        notifyRules(rulesToNotify, ruleSet.notified)

        /* apply changes to action services */
        val startedRules = rulesToRun - ruleSet.running
        val stoppedRules = ruleSet.running - rulesToRun
        applyRuleSetChange(startedRules, stoppedRules)
    }

    private fun applyRuleSetChange(startedRules: List<Rule>, stoppedRules: List<Rule>) {
        var stopAppBlockAction = false
        var stopNotificationAction = false
        var stopRingerAction = false
        var stopDndAction = false

        var appBlockAction: AppBlockAction? = null
        var appBlockRid: Int? = null
        var notificationAction: NotificationAction? = null
        var ringerMode: RingerMode? = null
        var dndAction: Boolean? = null

        startedRules.forEach {
            if (it.appBlockAction != null) {
                appBlockAction = it.appBlockAction
                appBlockRid = it.ruleInfo.ruleId
            }
            if (it.notificationAction != null) notificationAction = it.notificationAction
            if (it.dndAction != null) dndAction = true
            if (it.ringerAction != null) ringerMode = it.ringerAction.ringerMode
        }

        stoppedRules.forEach {
            if (it.appBlockAction != null) stopAppBlockAction = true
            if (it.notificationAction != null) stopNotificationAction = true
            if (it.dndAction != null) stopDndAction = true
            if (it.ringerAction != null) stopRingerAction = true
        }

        val ringerIntent by lazy { Intent(this, RingerService::class.java) }
        val notificationIntent by lazy { Intent(this, NotificationBlockService::class.java) }
        val appBlockIntent by lazy { Intent(this, AppBlockService::class.java) }

        /* RingerAction / DNDAction */
        if (ringerMode != null || dndAction != null || stopRingerAction || stopDndAction) {
            if (ringerMode != null)
                ringerIntent.putExtra(RingerService.RINGER_EXTRA_KEY, ringerMode as Parcelable)
            if (dndAction != null)
                ringerIntent.putExtra(RingerService.DND_EXTRA_KEY, true)

            ringerIntent.action = RingerService.RUN_ACTION
            startService(ringerIntent)
        }
        /* Notification Action */
        if (notificationAction != null) {
            notificationIntent.putExtra(
                NotificationBlockService.NOTIFICATION_EXTRA_KEY,
                notificationAction
            )
            startService(notificationIntent)
        } else if (stopNotificationAction) {
            notificationIntent.putExtra(
                NotificationBlockService.NOTIFICATION_EXTRA_KEY,
                null as NotificationAction?
            )
            startService(notificationIntent)
        }
        /* App Block Action */
        if (appBlockAction != null) {
            appBlockIntent.apply {
                action = AppBlockService.SUBMIT_APP_BLOCK_ACTION
                putExtra(AppBlockService.APP_BLOCK_EXTRA_KEY, appBlockAction)
                putExtra(AppBlockService.RID_EXTRA_KEY, appBlockRid)
            }
            startService(appBlockIntent)
        } else if (stopAppBlockAction) {
            appBlockIntent.apply {
                action = AppBlockService.SUBMIT_APP_BLOCK_ACTION
                putExtra(AppBlockService.APP_BLOCK_EXTRA_KEY, null as AppBlockAction?)
                putExtra(AppBlockService.RID_EXTRA_KEY, 0)
            }
            startService(appBlockIntent)
        }
    }

    private fun notifyRules(rulesToNotify: List<Rule>, notifiedRules: List<Rule>) {
        val removedRules = notifiedRules - rulesToNotify
        val newRules = rulesToNotify - notifiedRules

        removedRules.forEach {
            NotificationManagerCompat.from(this).cancel(it.ruleInfo.ruleId + 1)
        }

        newRules.forEach {
            val builder = NotificationCompat.Builder(this, RULE_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText("${it.ruleInfo.ruleName} 규칙이 수행됩니다.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(
                    Intent(this, MainService::class.java)
                        .apply {
                            action = RULE_EXEC_CONFIRM
                            putExtra(CONFIRMED_RULE_ID_KEY, it.ruleInfo.ruleId)
                        }
                        .let { intent ->
                            PendingIntent.getService(
                                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
                            )
                        }
                )
                .setAutoCancel(true)

            notificationManager.notify(it.ruleInfo.ruleId + 1, builder.build())
        }
    }

    private suspend fun confirmRuleExec(ruleId: Int) {
        val timeTriggerSet by lazy { getTimeTriggeredRules() }
        val activityTriggerSet by lazy { getActivityTriggeredRules() }
        val locationTriggerSet by lazy { getLocationTriggeredRules() }

        val rule = ruleRepo.getRuleByRid(ruleId)

        if (rule.timeTrigger != null && ruleId !in timeTriggerSet) return
        if (rule.locationTrigger != null && ruleId !in locationTriggerSet) return
        if (rule.activityTrigger != null && ruleId !in activityTriggerSet) return

        val ruleSet = getRuleSet()
        if (rule !in ruleSet.notified) return

        val rulesToRun =
            handleConflict(ruleSet.running + rule, ruleSet.conflicting, ruleSet.running)

        if (rule in rulesToRun) {
            updateRuleSet(
                RuleSet(ruleSet.notified - rule, ruleSet.conflicting, ruleSet.running + rule)
            )

            /* Notification Update */
            builder.setContentText(
                when (rulesToRun.size) {
                    0 -> "수행 중인 규칙이 없습니다."
                    1 -> "현재 [${rulesToRun[0].ruleInfo.ruleName}] 규칙이 수행 중입니다."
                    else -> "현재 [${rulesToRun[0].ruleInfo.ruleName}]외 ${rulesToRun.size - 1}개 규칙이 수행 중입니다."
                }
            )
            startForeground(1, builder.build())

            /* Apply Action */
            applyRuleSetChange(listOf(rule), emptyList())
        } else {
            updateRuleSet(
                RuleSet(ruleSet.notified - rule, ruleSet.conflicting + rule, ruleSet.running)
            )
        }
    }

    private fun handleConflict(
        rules: List<Rule>,
        conflictingRules: List<Rule>,
        runningRules: List<Rule>
    ): List<Rule> {
        /* Return true if the rule is a manual trigger rule */
        fun isManualTrigger(rule: Rule) = rule.locationTrigger == null
                && rule.timeTrigger == null
                && rule.activityTrigger == null

        /* Return a conflict-free rule list */
        tailrec fun removeConflictInNewRules(rules: List<Rule>): List<Rule> {
            return when {
                rules.count { it.appBlockAction != null } > 2 -> {
                    val appBlockRules = rules.filter { it.appBlockAction != null }
                    val selectedRule =
                        appBlockRules.firstOrNull { isManualTrigger(it) } ?: appBlockRules.random()
                    removeConflictInNewRules(rules - appBlockRules + selectedRule)
                }
                rules.count { it.notificationAction != null } > 2 -> {
                    val notificationRules = rules.filter { it.notificationAction != null }
                    val selectedRule =
                        notificationRules.firstOrNull { isManualTrigger(it) }
                            ?: notificationRules.random()
                    removeConflictInNewRules(rules - notificationRules + selectedRule)
                }
                rules.count { it.dndAction != null } > 2 -> {
                    val dndRules = rules.filter { it.dndAction != null }
                    val selectedRule =
                        dndRules.firstOrNull { isManualTrigger(it) } ?: dndRules.random()
                    removeConflictInNewRules(rules - dndRules + selectedRule)
                }
                rules.count { it.ringerAction != null } > 2 -> {
                    val ringerRules = rules.filter { it.ringerAction != null }
                    val selectedRule =
                        ringerRules.firstOrNull { isManualTrigger(it) } ?: ringerRules.random()
                    removeConflictInNewRules(rules - ringerRules + selectedRule)
                }
                else -> rules
            }
        }

        /* Remove conflicting rules from OldRules and return it */
        tailrec fun removeConflict(
            newRules: List<Rule>,
            oldRules: List<Rule>
        ): List<Rule> {
            val newAppBlockRule by lazy { newRules.firstOrNull { it.appBlockAction != null } }
            val oldAppBlockRule by lazy { oldRules.firstOrNull { it.appBlockAction != null } }
            val newNotificationRule by lazy { newRules.firstOrNull { it.notificationAction != null } }
            val oldNotificationRule by lazy { oldRules.firstOrNull { it.notificationAction != null } }
            val newDndRule by lazy { newRules.firstOrNull { it.dndAction != null } }
            val oldDndRule by lazy { oldRules.firstOrNull { it.dndAction != null } }
            val newRingerRule by lazy { newRules.firstOrNull { it.ringerAction != null } }
            val oldRingerRule by lazy { oldRules.firstOrNull { it.ringerAction != null } }

            return when {
                newAppBlockRule != null && oldAppBlockRule != null -> {
                    if (!isManualTrigger(newAppBlockRule!!) && isManualTrigger(oldAppBlockRule!!))
                        removeConflict(newRules - newAppBlockRule!!, oldRules)
                    else
                        removeConflict(newRules, oldRules - oldAppBlockRule!!)
                }
                newNotificationRule != null && oldNotificationRule != null -> {
                    if (!isManualTrigger(newNotificationRule!!)
                        && isManualTrigger(oldNotificationRule!!)
                    )
                        removeConflict(newRules - newNotificationRule!!, oldRules)
                    else
                        removeConflict(newRules, oldRules - oldNotificationRule!!)
                }
                newDndRule != null && oldDndRule != null -> {
                    if (!isManualTrigger(newDndRule!!) && isManualTrigger(oldDndRule!!))
                        removeConflict(newRules - newDndRule!!, oldRules)
                    else
                        removeConflict(newRules, oldRules - oldDndRule!!)
                }
                newRingerRule != null && oldRingerRule != null -> {
                    if (!isManualTrigger(newRingerRule!!) && isManualTrigger(oldRingerRule!!))
                        removeConflict(newRules - newRingerRule!!, oldRules)
                    else
                        removeConflict(newRules, oldRules - oldRingerRule!!)
                }
                else -> newRules + oldRules
            }
        }

        val newRules = rules - conflictingRules - runningRules
        val oldRules = (rules - newRules).filter { it in runningRules }

        val filteredNewRules = removeConflictInNewRules(newRules)
        return removeConflict(filteredNewRules, oldRules)
    }

    private fun startTriggerObserving() {
        val timeIntent = Intent(this, TimeTriggerService::class.java)
        val activityIntent = Intent(this, ActivityTriggerService::class.java)
        val locationIntent = Intent(this, LocationTriggerService::class.java)

        startService(timeIntent)
        startService(activityIntent)
        startService(locationIntent)
    }

    companion object {
        const val CHANNEL_ID = "focus_aid_service_channel"
        const val RULE_NOTIFICATION_CHANNEL_ID = "focus_aid_rule_notification_channel"

        const val TIME_TRIGGER = "TIME_TRIGGER"
        const val ACTIVITY_TRIGGER = "ACTIVITY_TRIGGER"
        const val LOCATION_TRIGGER = "LOCATION_TRIGGER"
        const val START_BACKGROUND = "START_BACKGROUND"
        const val RULE_CHANGE = "RULE_CHANGE"
        const val RULE_EXEC_CONFIRM = "RULE_EXEC_CONFIRM"

        const val MAIN_SERVICE_PREF_NAME = "com.yeongil.focusaid.MAIN_SERVICE"

        const val TIMESTAMP_TIME_TRIGGER_KEY = "TIMESTAMP_TIME_TRIGGER"
        const val TIMESTAMP_ACTIVITY_TRIGGER_KEY = "TIMESTAMP_ACTIVITY_TRIGGER"
        const val TIMESTAMP_LOCATION_TRIGGER_KEY = "TIMESTAMP_LOCATION_TRIGGER"

        const val TIME_TRIGGERED_RULES_KEY = "TIME_TRIGGERED_RULES"
        const val ACTIVITY_TRIGGERED_RULES_KEY = "ACTIVITY_TRIGGERED_RULES"
        const val LOCATION_TRIGGERED_RULES_KEY = "LOCATION_TRIGGERED_RULES"

        const val TIMESTAMP_RULE_SET_KEY = "TIMESTAMP_RULE_SET"
        const val RULE_SET_KEY = "RULE_SET"

        const val CONFIRMED_RULE_ID_KEY = "CONFIRMED_RULE_ID"
        const val CHANGED_RULE_ID_KEY = "CHANGED_RULE_ID"
        const val DEFAULT_RINGER_KEY = "DEFAULT_RINGER"
        const val DEFAULT_DND_KEY = "DEFAULT_DND"


        val emptySetStr = Json.encodeToString(emptySet<Unit>())
        val ruleSetDefaultStr = Json.encodeToString(RuleSet())
    }
}