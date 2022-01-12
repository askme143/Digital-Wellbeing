package com.yeongil.focusaid.background

import android.app.*
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
import com.yeongil.focusaid.BuildConfig
import com.yeongil.focusaid.MainActivity
import com.yeongil.focusaid.R
import com.yeongil.focusaid.background.data.RuleState
import com.yeongil.focusaid.data.rule.Rule
import com.yeongil.focusaid.data.rule.action.*
import com.yeongil.focusaid.dataSource.SequenceNumber
import com.yeongil.focusaid.dataSource.ruleDatabase.RuleDatabase
import com.yeongil.focusaid.repository.RuleRepository
import com.yeongil.focusaid.utils.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class MainService : LifecycleService() {
    private val initEvent = Event(Unit)
    private val sharedPref by lazy { getSharedPreferences(MAIN_SERVICE_PREF_NAME, MODE_PRIVATE) }
    private val ruleRepo by lazy {
        RuleRepository(SequenceNumber(this), RuleDatabase.getInstance(this).ruleCombinedDao())
    }

    private val midnightIntent by lazy {
        Intent(this, MainService::class.java)
            .apply { action = MIDNIGHT_RESET }
            .let {
                PendingIntent.getService(
                    this,
                    MIDNIGHT_REQ_CODE,
                    it,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }
    }
    private val foregroundNotificationBuilder by lazy {
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText("실행 중")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(
                TaskStackBuilder.create(this).run {
                    addNextIntentWithParentStack(
                        Intent(this@MainService, MainActivity::class.java)
                    )
                    getPendingIntent(NOTI_BUILDER_REQ_CODE, 0)
                }
            )
            .setOngoing(true)
    }

    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    private val alarmManager by lazy { getSystemService(ALARM_SERVICE) as AlarmManager }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        when (intent?.action) {
            TIME_TRIGGER -> {
                Log.e("hello", TIME_TRIGGER)
                val intentStr = intent.getStringExtra(TIME_TRIGGERED_RULES_KEY) ?: emptySetStr
                val ruleIdSet = Json.decodeFromString<Set<Int>>(intentStr)

                updateTimeTriggeredRules(intentStr)
                lifecycleScope.launch(Dispatchers.Default) {
                    onTriggerUpdate(getTriggeredRules(timeTriggerSet = ruleIdSet))
                }
            }
            ACTIVITY_TRIGGER -> {
                Log.e("hello", ACTIVITY_TRIGGER)
                val intentStr = intent.getStringExtra(ACTIVITY_TRIGGERED_RULES_KEY) ?: emptySetStr
                val ruleIdSet = Json.decodeFromString<Set<Int>>(intentStr)

                updateActivityTriggeredRules(intentStr)
                lifecycleScope.launch(Dispatchers.Default) {
                    onTriggerUpdate(getTriggeredRules(activityTriggerSet = ruleIdSet))
                }
            }
            LOCATION_TRIGGER -> {
                Log.e("hello", LOCATION_TRIGGER)
                val intentStr = intent.getStringExtra(LOCATION_TRIGGERED_RULES_KEY) ?: emptySetStr
                val ruleIdSet = Json.decodeFromString<Set<Int>>(intentStr)

                updateLocationTriggeredRules(intentStr)
                lifecycleScope.launch(Dispatchers.Default) {
                    onTriggerUpdate(getTriggeredRules(locationTriggerSet = ruleIdSet))
                }
            }
            START_BACKGROUND -> {
                initEvent.getContentIfNotHandled()?.let {
                    Log.e("hello", START_BACKGROUND)
                    reset()
                    startTriggerObserving()
                }
            }
            RULE_CHANGE -> {
                Log.e("hello", RULE_CHANGE)
                startTriggerObserving()
            }
            RULE_EXEC_ACCEPT -> {
                Log.e("hello", RULE_EXEC_ACCEPT)

                val ruleId = intent.getIntExtra(CONFIRMED_RULE_ID_KEY, 0)
                Log.e("hello", "Rule Execution Accepted: $ruleId")

                lifecycleScope.launch(Dispatchers.Default) {
                    if (ruleId != 0) onRuleExecAccepted(ruleRepo.getRuleByRid(ruleId))
                }
            }
            RULE_EXEC_REJECT -> {
                Log.e("hello", RULE_EXEC_REJECT)

                val ruleId = intent.getIntExtra(CONFIRMED_RULE_ID_KEY, 0)
                Log.e("hello", "Rule Execution Rejected: $ruleId")

                lifecycleScope.launch(Dispatchers.Default) {
                    if (ruleId != 0) onRuleExecRejected(ruleRepo.getRuleByRid(ruleId))
                }
            }
            MIDNIGHT_RESET -> {
                Log.e("hello", MIDNIGHT_RESET)

                resetManualTriggerRules()

                val calendar = Calendar.getInstance().apply {
                    add(Calendar.DATE, 1)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    midnightIntent
                )
            }
        }

        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        initNotificationChannel()
        initRingerChangeReceiver()
        initMidnightAlarm()

        startForeground(1, foregroundNotificationBuilder.build())
    }

    private fun startTriggerObserving() {
        val timeIntent = Intent(this, TimeTriggerService::class.java)
        val activityIntent = Intent(this, ActivityTriggerService::class.java)
        val locationIntent = Intent(this, LocationTriggerService::class.java)

        startService(timeIntent)
        startService(activityIntent)
        startService(locationIntent)
    }

    private fun onTriggerUpdate(triggeredRuleSet: Set<Rule>) {
        val oldRuleState = getRuleState()

        val alreadyConflicted = triggeredRuleSet intersect oldRuleState.conflicting
        val notifySet = triggeredRuleSet
            .subtract(oldRuleState.conflicting)
            .subtract(oldRuleState.running)
            .filter { it.ruleInfo.notiOnTrigger }
            .toSet()

        val candidates = triggeredRuleSet subtract (notifySet union alreadyConflicted)
        val runSet = handleConflict(oldRuleState.running, candidates)

        val conflictSet = triggeredRuleSet - notifySet - runSet

        val newRuleState = RuleState(notifySet, conflictSet, runSet)

        applyNewRuleState(oldRuleState, newRuleState)
    }

    private fun onRuleExecAccepted(rule: Rule) {
        fun isTriggered(rule: Rule): Boolean {
            return (rule.timeTrigger == null || rule.ruleInfo.ruleId in getTimeTriggeredRules())
                    && (rule.locationTrigger == null || rule.ruleInfo.ruleId in getLocationTriggeredRules())
                    && (rule.activityTrigger == null || rule.ruleInfo.ruleId in getActivityTriggeredRules())
        }

        fun isNotified(rule: Rule): Boolean {
            return rule in getRuleState().notified
        }

        if (!isTriggered(rule) || !isNotified(rule)) return

        val oldRuleState = getRuleState()
        val candidates = oldRuleState.running + rule

        val runSet = handleConflict(oldRuleState.running, candidates)
        val notifySet = oldRuleState.notified - rule
        val conflictSet =
            if (rule in runSet) oldRuleState.conflicting else oldRuleState.conflicting + rule

        val newRuleState = RuleState(notifySet, conflictSet, runSet)

        applyNewRuleState(oldRuleState, newRuleState)
    }

    private fun onRuleExecRejected(rule: Rule) {
        val oldRuleState = getRuleState()
        val newRuleState = RuleState(
            oldRuleState.notified - rule,
            oldRuleState.conflicting + rule,
            oldRuleState.running
        )

        applyNewRuleState(oldRuleState, newRuleState)
    }

    private fun applyNewRuleState(oldRuleState: RuleState, newRuleState: RuleState) {
        updateRuleState(newRuleState)
        updateActions(oldRuleState.running, newRuleState.running)
        notifyRules(oldRuleState.notified, newRuleState.notified)
        updateForegroundNotification(newRuleState.running)
    }

    private fun updateActions(oldRunningSet: Set<Rule>, newRunningSet: Set<Rule>) {
        val removedRules = oldRunningSet subtract newRunningSet
        val newRules = newRunningSet subtract oldRunningSet

        updateAppBlockAction(
            removedRules.firstOrNull { it.appBlockAction != null },
            newRules.firstOrNull { it.appBlockAction != null })

        updateNotificationAction(
            removedRules.firstOrNull { it.notificationAction != null },
            newRules.firstOrNull { it.notificationAction != null })

        updateRingerAction(
            removedRules.firstOrNull { it.ringerAction != null },
            newRules.firstOrNull { it.ringerAction != null })

        updateDndAction(
            removedRules.firstOrNull { it.dndAction != null },
            newRules.firstOrNull { it.dndAction != null })
    }

    private fun notifyRules(oldNotifiedSet: Set<Rule>, newNotifiedSet: Set<Rule>) {
        val notificationBuilder = NotificationCompat.Builder(this, RULE_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val rejects = oldNotifiedSet subtract newNotifiedSet
        val notifies = newNotifiedSet subtract oldNotifiedSet

        fun notificationId(rule: Rule): Int {
            return rule.ruleInfo.ruleId
        }

        fun notificationText(rule: Rule): String {
            return "${rule.ruleInfo.ruleName} 규칙이 수행됩니다."
        }

        fun makePendingIntent(rule: Rule, action: String): PendingIntent? {
            return PendingIntent.getService(
                this,
                rule.ruleInfo.ruleId * 2 + REQ_CODE_OFFSET,
                Intent(this, MainService::class.java)
                    .apply {
                        this.action = action
                        putExtra(CONFIRMED_RULE_ID_KEY, rule.ruleInfo.ruleId)
                    },
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        rejects.forEach { NotificationManagerCompat.from(this).cancel(notificationId(it)) }
        notifies.forEach {
            val builder = notificationBuilder
                .setContentText(notificationText(it))
                .setContentIntent(makePendingIntent(it, RULE_EXEC_ACCEPT))
                .setDeleteIntent(makePendingIntent(it, RULE_EXEC_REJECT))
                .setAutoCancel(true)

            notificationManager.notify(notificationId(it), builder.build())
        }
    }

    private fun updateForegroundNotification(runningRules: Set<Rule>) {
        with(runningRules) {
            foregroundNotificationBuilder.setContentText(
                when (this.size) {
                    0 -> "수행 중인 규칙이 없습니다."
                    1 -> "현재 [${this.first().ruleInfo.ruleName}] 규칙이 수행 중입니다."
                    else -> "현재 [${this.first().ruleInfo.ruleName}]외 ${this.size - 1}개 규칙이 수행 중입니다."
                }
            )
        }
        foregroundNotificationBuilder.setWhen(System.currentTimeMillis())
        notificationManager.notify(1, foregroundNotificationBuilder.build())
    }

    private fun handleConflict(runningRules: Set<Rule>, candidates: Set<Rule>): Set<Rule> {
        tailrec fun selectNewRunningRules(
            candidatesWithPriority: List<Pair<Rule, Int>>,
        ): Set<Rule> {
            val conflicts: Set<Pair<Rule, Int>> = when {
                candidatesWithPriority.count { (rule, _) -> rule.appBlockAction != null } > 1 -> {
                    candidatesWithPriority.filter { (rule, _) -> rule.appBlockAction != null }
                        .toSet()
                }
                candidatesWithPriority.count { (rule, _) -> rule.notificationAction != null } > 1 -> {
                    candidatesWithPriority.filter { (rule, _) -> rule.notificationAction != null }
                        .toSet()
                }
                candidatesWithPriority.count { (rule, _) -> rule.dndAction != null } > 1 -> {
                    candidatesWithPriority.filter { (rule, _) -> rule.dndAction != null }
                        .toSet()
                }
                candidatesWithPriority.count { (rule, _) -> rule.ringerAction != null } > 1 -> {
                    candidatesWithPriority.filter { (rule, _) -> rule.ringerAction != null }
                        .toSet()
                }
                else -> {
                    return candidatesWithPriority.map { it.first }.toSet()
                }
            }

            val selectedRule = conflicts.maxByOrNull { (_, priority) -> priority }!!
            return selectNewRunningRules(candidatesWithPriority - conflicts + selectedRule)
        }

        val candidatesWithPriority = candidates.map { rule ->
            Pair(rule, calcRulePriority(rule, rule !in runningRules))
        }

        return selectNewRunningRules(candidatesWithPriority)
    }

    private fun calcRulePriority(rule: Rule, isNew: Boolean): Int {
        val manualTriggerFirst = if (rule.locationTrigger == null
            && rule.timeTrigger == null
            && rule.activityTrigger == null
        ) 10 else 0
        val oldRuleFirst = if (isNew) 0 else 1

        return manualTriggerFirst + oldRuleFirst
    }

    private fun initNotificationChannel() {
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
    }

    private fun initRingerChangeReceiver() {
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
    }

    private fun initMidnightAlarm() {
        /* Call when it's midnight */
        val calendar = Calendar.getInstance().apply {
            add(Calendar.DATE, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            midnightIntent
        )
    }

    private fun reset() {
        updateLocationTriggeredRules(emptySetStr)
        updateActivityTriggeredRules(emptySetStr)
        updateTimeTriggeredRules(emptySetStr)
        updateRuleState(RuleState())
    }

    private fun resetManualTriggerRules() {
        val ruleState = getRuleState()

        /* Reset AppBlockAction of ManualTriggerRule */
        val appBlockManualRule = ruleState.running
            .firstOrNull {
                it.appBlockAction != null && it.timeTrigger == null
                        && it.locationTrigger == null && it.activityTrigger == null
            }
        if (appBlockManualRule != null) {
            val rid = appBlockManualRule.ruleInfo.ruleId
            val appBlockAction = appBlockManualRule.appBlockAction ?: return
            val appBlockIntent by lazy { Intent(this, AppBlockService::class.java) }

            appBlockIntent.apply {
                action = AppBlockService.SUBMIT_APP_BLOCK_ACTION
                putExtra(AppBlockService.APP_BLOCK_EXTRA_KEY, appBlockAction)
                putExtra(AppBlockService.RID_EXTRA_KEY, rid)
            }
            startService(appBlockIntent)
        }
    }

    private fun updateAppBlockAction(oldRule: Rule?, newRule: Rule?) {
        val appBlockIntent by lazy { Intent(this, AppBlockService::class.java) }

        if (newRule != null) {
            appBlockIntent.apply {
                action = AppBlockService.SUBMIT_APP_BLOCK_ACTION
                putExtra(AppBlockService.APP_BLOCK_EXTRA_KEY, newRule.appBlockAction)
                putExtra(AppBlockService.RID_EXTRA_KEY, newRule.ruleInfo.ruleId)
            }
            startService(appBlockIntent)
        } else if (oldRule != null) {
            appBlockIntent.apply {
                action = AppBlockService.SUBMIT_APP_BLOCK_ACTION
                putExtra(AppBlockService.APP_BLOCK_EXTRA_KEY, null as AppBlockAction?)
                putExtra(AppBlockService.RID_EXTRA_KEY, 0)
            }
            startService(appBlockIntent)
        }
    }

    private fun updateNotificationAction(oldRule: Rule?, newRule: Rule?) {
        val notificationIntent by lazy { Intent(this, NotificationBlockService::class.java) }
        if (newRule != null) {
            notificationIntent.putExtra(
                NotificationBlockService.NOTIFICATION_EXTRA_KEY,
                newRule.notificationAction
            )
            startService(notificationIntent)
        } else if (oldRule != null) {
            notificationIntent.putExtra(
                NotificationBlockService.NOTIFICATION_EXTRA_KEY,
                null as NotificationAction?
            )
            startService(notificationIntent)
        }
    }

    private fun updateRingerAction(oldRule: Rule?, newRule: Rule?) {
        val ringerIntent by lazy { Intent(this, RingerService::class.java) }

        if (newRule?.ringerAction != null) {
            ringerIntent.putExtra(
                RingerService.RINGER_EXTRA_KEY,
                newRule.ringerAction.ringerMode as Parcelable
            )
            if (oldRule != null) {
                ringerIntent.action = RingerService.CHANGE_ACTION
            } else {
                ringerIntent.action = RingerService.RUN_ACTION
            }
        } else if (oldRule != null) {
            ringerIntent.action = RingerService.STOP_ACTION
        }

        startService(ringerIntent)
    }

    private fun updateDndAction(oldRule: Rule?, newRule: Rule?) {
        val dndIntent by lazy { Intent(this, DndService::class.java) }

        if (newRule != null) {
            dndIntent.putExtra(DndService.DND_EXTRA_KEY, true)
            dndIntent.action = DndService.RUN_ACTION
            startService(dndIntent)
        } else if (oldRule != null) {
            dndIntent.putExtra(DndService.DND_EXTRA_KEY, false)
            dndIntent.action = DndService.RUN_ACTION
            startService(dndIntent)
        }
    }

    private suspend fun getTriggeredRules(
        timeTriggerSet: Set<Int> = getTimeTriggeredRules(),
        activityTriggerSet: Set<Int> = getActivityTriggeredRules(),
        locationTriggerSet: Set<Int> = getLocationTriggeredRules(),
    ): Set<Rule> {
        val rules = ruleRepo.getActiveRuleList().toSet()

        return rules
            .filter { it.timeTrigger == null || it.ruleInfo.ruleId in timeTriggerSet }
            .filter { it.activityTrigger == null || it.ruleInfo.ruleId in activityTriggerSet }
            .filter { it.locationTrigger == null || it.ruleInfo.ruleId in locationTriggerSet }
            .toSet()
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

    private fun getRuleState(): RuleState {
        return Json.decodeFromString(
            sharedPref.getString(RULE_STATE_KEY, ruleSetDefaultStr) ?: ruleSetDefaultStr
        )
    }

    private fun updateRuleState(ruleState: RuleState) {
        sharedPref.edit {
            putString(RULE_STATE_KEY, Json.encodeToString(ruleState))
            putLong(TIMESTAMP_RULE_STATE_KEY, System.currentTimeMillis())
            commit()
        }
    }

    companion object {
        const val CHANNEL_ID = "focus_aid_service_channel"
        const val RULE_NOTIFICATION_CHANNEL_ID = "focus_aid_rule_notification_channel"

        const val TIME_TRIGGER = "${BuildConfig.APPLICATION_ID}.TIME_TRIGGER"
        const val ACTIVITY_TRIGGER = "${BuildConfig.APPLICATION_ID}.ACTIVITY_TRIGGER"
        const val LOCATION_TRIGGER = "${BuildConfig.APPLICATION_ID}.LOCATION_TRIGGER"
        const val START_BACKGROUND = "${BuildConfig.APPLICATION_ID}.START_BACKGROUND"
        const val RULE_CHANGE = "${BuildConfig.APPLICATION_ID}.RULE_CHANGE"
        const val RULE_EXEC_ACCEPT = "${BuildConfig.APPLICATION_ID}.RULE_EXEC_ACCEPT"
        const val RULE_EXEC_REJECT = "${BuildConfig.APPLICATION_ID}.RULE_EXEC_REJECT"
        const val MIDNIGHT_RESET = "${BuildConfig.APPLICATION_ID}.MIDNIGHT_RESET"

        const val MAIN_SERVICE_PREF_NAME = "${BuildConfig.APPLICATION_ID}.MAIN_SERVICE"

        const val TIMESTAMP_TIME_TRIGGER_KEY = "TIMESTAMP_TIME_TRIGGER"
        const val TIMESTAMP_ACTIVITY_TRIGGER_KEY = "TIMESTAMP_ACTIVITY_TRIGGER"
        const val TIMESTAMP_LOCATION_TRIGGER_KEY = "TIMESTAMP_LOCATION_TRIGGER"

        const val TIME_TRIGGERED_RULES_KEY = "TIME_TRIGGERED_RULES"
        const val ACTIVITY_TRIGGERED_RULES_KEY = "ACTIVITY_TRIGGERED_RULES"
        const val LOCATION_TRIGGERED_RULES_KEY = "LOCATION_TRIGGERED_RULES"

        const val TIMESTAMP_RULE_STATE_KEY = "TIMESTAMP_RULE_SET"
        const val RULE_STATE_KEY = "RULE_STATE"

        const val CONFIRMED_RULE_ID_KEY = "CONFIRMED_RULE_ID"
        const val CHANGED_RULE_ID_KEY = "CHANGED_RULE_ID"

        /* Request codes for pending intents */
        const val NOTI_BUILDER_REQ_CODE = 0
        const val MIDNIGHT_REQ_CODE = 1
        const val REQ_CODE_OFFSET = 2


        val emptySetStr = Json.encodeToString(emptySet<Unit>())
        val ruleSetDefaultStr = Json.encodeToString(RuleState())
    }
}