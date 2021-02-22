package com.yeongil.digitalwellbeing.background

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.edit
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.yeongil.digitalwellbeing.MainActivity
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.background.data.RuleSet
import com.yeongil.digitalwellbeing.data.rule.Rule
import com.yeongil.digitalwellbeing.data.rule.action.*
import com.yeongil.digitalwellbeing.dataSource.SequenceNumber
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.RuleDatabase
import com.yeongil.digitalwellbeing.repository.RuleRepository
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
                    getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
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
                Log.e("hello", intentStr)
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
                startTriggerObserving()
            }
            RULE_CHANGE -> {
                Log.e("hello", "RULE_CHANGE")
                startTriggerObserving()
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
                "Digitall Wellbeing Channel",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Digital Wellbeing Background"
                setShowBadge(false)
            }
            notificationManager.createNotificationChannel(serviceChannel)

            /* Create Notification Channel for Notified Rules */
            val notifyChannel = NotificationChannel(
                RULE_NOTIFICATION_CHANNEL_ID,
                "Digitall Wellbeing Rule Confirm Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Digital Wellbeing Rule Execution Confirm"
            }

            notificationManager.createNotificationChannel(notifyChannel)
        }

        startForeground(1, builder.build())
    }

    private fun getTimeTriggeredRules(): Set<Int> {
        val set = Json.decodeFromString<Set<Int>>(
            sharedPref.getString(TIME_TRIGGERED_RULES_KEY, emptySetStr) ?: emptySetStr
        )
        val timestamp = sharedPref.getLong(TIMESTAMP_TIME_TRIGGER_KEY, 0)

        return if (timestamp < System.currentTimeMillis() - 20 * 1000) emptySet()
        else set
    }

    private fun getActivityTriggeredRules(): Set<Int> {
        val set = Json.decodeFromString<Set<Int>>(
            sharedPref.getString(ACTIVITY_TRIGGERED_RULES_KEY, emptySetStr) ?: emptySetStr
        )
        val timestamp = sharedPref.getLong(TIMESTAMP_ACTIVITY_TRIGGER_KEY, 0)

        return if (timestamp < System.currentTimeMillis() - 20 * 1000) emptySet()
        else set
    }

    private fun getLocationTriggeredRules(): Set<Int> {
        val str = sharedPref.getString(LOCATION_TRIGGERED_RULES_KEY, emptySetStr) ?: emptySetStr
        val set = Json.decodeFromString<Set<Int>>(
            sharedPref.getString(LOCATION_TRIGGERED_RULES_KEY, emptySetStr) ?: emptySetStr
        )
        val timestamp = sharedPref.getLong(TIMESTAMP_LOCATION_TRIGGER_KEY, 0)

        return if (timestamp < System.currentTimeMillis() - 20 * 1000) emptySet()
        else set
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

    private fun getDefaultRingerMode(): RingerMode {
        return sharedPref.getString(DEFAULT_RINGER_KEY, Json.encodeToString(RingerMode.VIBRATE))
            ?.let { Json.decodeFromString<RingerMode>(it) }
            ?: RingerMode.VIBRATE
    }

    private fun updateDefaultRinger(ringerMode: RingerMode) {
        sharedPref.edit {
            putString(DEFAULT_RINGER_KEY, Json.encodeToString(ringerMode))
            commit()
        }
    }

    private fun getDefaultDND(): Boolean {
        return sharedPref.getBoolean(DEFAULT_DND_KEY, false)
    }

    private fun updateDefaultRinger(dndMode: Boolean) {
        sharedPref.edit {
            putBoolean(DEFAULT_DND_KEY, dndMode)
            commit()
        }
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

        /* make notification */
        notifyRules(rulesToNotify, ruleSet.notified)

        /* apply changes to action services */
        val startedRules = rulesToRun - ruleSet.running
        val stoppedRules = ruleSet.running - rulesToRun
        applyRuleChange(startedRules, stoppedRules)
    }

    private fun applyRuleChange(startedRules: List<Rule>, stoppedRules: List<Rule>) {
        /* TODO: Make ActionServices */
        /* TODO: Pass Delta Results so that services can apply the changes */
        /* TODO: Design MainService - ActionServices Protocol */

        var stopAppBlockAction = false
        var stopNotificationAction = false
        var stopRingerAction = false
        var stopDndAction = false

        var appBlockAction: AppBlockAction? = null
        var notificationAction: NotificationAction? = null
        var ringerMode: RingerMode? = null
        var dndAction: Boolean? = null

        startedRules.forEach {
            if (it.appBlockAction != null) {
                appBlockAction = it.appBlockAction
            }
            if (it.notificationAction != null) {
                notificationAction = it.notificationAction
            }
            if (it.dndAction != null) {
                dndAction = true
            }
            if (it.ringerAction != null) {
                ringerMode = it.ringerAction.ringerMode
            }
        }

        stoppedRules.forEach {
            if (it.appBlockAction != null) {
                stopAppBlockAction = true
            }
            if (it.notificationAction != null) {
                stopNotificationAction = true
            }
            if (it.dndAction != null) {
                stopDndAction = true
            }
            if (it.ringerAction != null) {
                stopRingerAction = true
            }
        }

        if (ringerMode != null || dndAction != null || stopRingerAction || stopDndAction) {
            val ringerIntent = Intent(this, RingerService::class.java)

            if (ringerMode != null)
                ringerIntent.putExtra(RingerService.RINGER_EXTRA_KEY, ringerMode)
            else if (stopRingerAction)
                ringerIntent.putExtra(RingerService.RINGER_EXTRA_KEY, getDefaultRingerMode())

            if (dndAction != null)
                ringerIntent.putExtra(RingerService.DND_EXTRA_KEY, true)
            else if (stopDndAction)
                ringerIntent.putExtra(RingerService.DND_EXTRA_KEY, getDefaultDND())

            startService(ringerIntent)
        }
    }

    private fun notifyRules(rulesToNotify: List<Rule>, notifiedRules: List<Rule>) {
        val removedRules = notifiedRules - rulesToNotify
        val newRules = rulesToNotify - notifiedRules

        removedRules.forEach {
            NotificationManagerCompat.from(this).cancel(it.ruleInfo.ruleId + 1)
        }

        newRules.forEach {
            val builder = NotificationCompat.Builder(this, MainService.RULE_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText("${it.ruleInfo.ruleName} 규칙이 수행됩니다.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(
                    TaskStackBuilder.create(this).run {
                        addNextIntentWithParentStack(
                            /* TODO: Show Rule Start Confirm Dialog */
                            Intent(this@MainService, MainActivity::class.java)
                        )
                        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
                    }
                )

            notificationManager.notify(it.ruleInfo.ruleId + 1, builder.build())
        }
    }

    private fun handleConflict(
        rules: List<Rule>,
        conflictingRules: List<Rule>,
        runningRules: List<Rule>
    ): List<Rule> {
        /* Return a conflict-free rule list */
        tailrec fun removeConflictInNewRules(rules: List<Rule>): List<Rule> {
            return when {
                rules.count { it.appBlockAction != null } > 2 -> {
                    val appBlockRules = rules.filter { it.appBlockAction != null }
                    removeConflictInNewRules(rules - appBlockRules + appBlockRules.random())
                }
                rules.count { it.notificationAction != null } > 2 -> {
                    val notificationRules = rules.filter { it.notificationAction != null }
                    removeConflictInNewRules(rules - notificationRules + notificationRules.random())
                }
                rules.count { it.dndAction != null } > 2 -> {
                    val dndRules = rules.filter { it.dndAction != null }
                    removeConflictInNewRules(rules - dndRules + dndRules.random())
                }
                rules.count { it.ringerAction != null } > 2 -> {
                    val ringerRules = rules.filter { it.ringerAction != null }
                    removeConflictInNewRules(rules - ringerRules + ringerRules.random())
                }
                else -> rules
            }
        }

        /* Remove conflicting rules from OldRules and return it */
        tailrec fun removeConflictInOldRules(
            newRules: List<Rule>,
            oldRules: List<Rule>
        ): List<Rule> {
            return when {
                newRules.any { it.appBlockAction != null }
                        && oldRules.any { it.appBlockAction != null } -> {
                    removeConflictInOldRules(
                        newRules,
                        oldRules.filter { it.appBlockAction == null })
                }
                newRules.any { it.notificationAction != null }
                        && oldRules.any { it.notificationAction != null } -> {
                    removeConflictInOldRules(
                        newRules,
                        oldRules.filter { it.notificationAction == null })
                }
                newRules.any { it.dndAction != null }
                        && oldRules.any { it.dndAction != null } -> {
                    removeConflictInOldRules(newRules, oldRules.filter { it.dndAction == null })
                }
                newRules.any { it.ringerAction != null }
                        && oldRules.any { it.ringerAction != null } -> {
                    removeConflictInOldRules(newRules, oldRules.filter { it.ringerAction == null })
                }
                else ->
                    oldRules
            }
        }

        val newRules = rules - conflictingRules - runningRules
        val oldRules = (rules - newRules).filter { it in runningRules }

        val newRulesToRun = removeConflictInNewRules(newRules)
        val oldRulesToRun = runningRules
            .filter { it in oldRules }
            .let { removeConflictInOldRules(newRulesToRun, it) }

        return newRulesToRun + oldRulesToRun
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
        const val CHANNEL_ID = "digital_wellbeing_service_channel"
        const val RULE_NOTIFICATION_CHANNEL_ID = "digital_wellbeing_rule_notification_channel"

        const val TIME_TRIGGER = "TIME_TRIGGER"
        const val ACTIVITY_TRIGGER = "ACTIVITY_TRIGGER"
        const val LOCATION_TRIGGER = "LOCATION_TRIGGER"
        const val START_BACKGROUND = "START_BACKGROUND"
        const val RULE_CHANGE = "RULE_CHANGE"

        const val MAIN_SERVICE_PREF_NAME = "com.yeongil.digitalwellbeing.MAIN_SERVICE"

        const val TIMESTAMP_TIME_TRIGGER_KEY = "TIMESTAMP_TIME_TRIGGER"
        const val TIMESTAMP_ACTIVITY_TRIGGER_KEY = "TIMESTAMP_ACTIVITY_TRIGGER"
        const val TIMESTAMP_LOCATION_TRIGGER_KEY = "TIMESTAMP_LOCATION_TRIGGER"

        const val TIME_TRIGGERED_RULES_KEY = "TIME_TRIGGERED_RULES"
        const val ACTIVITY_TRIGGERED_RULES_KEY = "ACTIVITY_TRIGGERED_RULES"
        const val LOCATION_TRIGGERED_RULES_KEY = "LOCATION_TRIGGERED_RULES"

        const val TIMESTAMP_RULE_SET_KEY = "TIMESTAMP_RULE_SET"
        const val RULE_SET_KEY = "RULE_SET"

        const val CHANGED_RULE_ID_KEY = "CHANGED_RULE_ID"
        const val DEFAULT_RINGER_KEY = "DEFAULT_RINGER"
        const val DEFAULT_DND_KEY = "DEFAULT_DND"


        val emptySetStr = Json.encodeToString(emptySet<Int>())
        val ruleSetDefaultStr = Json.encodeToString(RuleSet())
    }
}