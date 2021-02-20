package com.yeongil.digitalwellbeing.background

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.edit
import androidx.lifecycle.LifecycleService
import com.yeongil.digitalwellbeing.MainActivity
import com.yeongil.digitalwellbeing.MainService
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.data.rule.Rule
import com.yeongil.digitalwellbeing.dataSource.SequenceNumber
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.RuleDatabase
import com.yeongil.digitalwellbeing.repository.RuleRepository
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainService : LifecycleService() {
    private val sharedPref by lazy { getSharedPreferences(MAIN_SERVICE_PREF_NAME, MODE_PRIVATE) }
    private val ruleRepo by lazy {
        RuleRepository(SequenceNumber(this), RuleDatabase.getInstance(this).ruleDao())
    }
    private val builder by lazy {
        NotificationCompat.Builder(this, MainNotification.CHANNEL_ID)
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

    /* System Services */
    private val notificationManager =
        this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        when (intent?.action) {
            TIME_TRIGGER -> {
                val intentStr = intent.getStringExtra(TIME_TRIGGERED_RUES_KEY) ?: emptySetStr
                val intentMap =
                    Json.decodeFromString<Set<Int>>(intentStr)

                updateTimeTriggeredRules(intentStr)
            }
            ACTIVITY_TRIGGER -> {
                val intentStr = intent.getStringExtra(ACTIVITY_TRIGGERED_RUES_KEY) ?: emptySetStr
                val intentMap =
                    Json.decodeFromString<Set<Int>>(intentStr)

                updateActivityTriggeredRules(intentStr)
            }
            LOCATION_TRIGGER -> {
                val intentStr = intent.getStringExtra(LOCATION_TRIGGERED_RUES_KEY) ?: emptySetStr
                val rule =
                    Json.decodeFromString<Set<Int>>(intentStr)

                updateLocationTriggeredRules(intentStr)
            }
        }

        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create Notification Channel for ForegroundService */
            val serviceChannel = NotificationChannel(
                MainNotification.CHANNEL_ID,
                "Digitall Wellbeing Channel",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Digital Wellbeing Background"
                setShowBadge(false)
            }
            notificationManager.createNotificationChannel(serviceChannel)

            /* Create Notification Channel for Notified Rules */
            val notifyChannel = NotificationChannel(
                MainService.RULE_NOTIFICATION_CHANNEL_ID,
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
            sharedPref.getString(TIME_TRIGGERED_RUES_KEY, emptySetStr) ?: emptySetStr
        )
        val timestamp = sharedPref.getLong(TIMESTAMP_TIME_TRIGGER_KEY, 0)

        return if (timestamp < System.currentTimeMillis() - 20 * 1000) emptySet()
        else set
    }

    private fun getActivityTriggeredRules(): Set<Int> {
        val set = Json.decodeFromString<Set<Int>>(
            sharedPref.getString(ACTIVITY_TRIGGERED_RUES_KEY, emptySetStr) ?: emptySetStr
        )
        val timestamp = sharedPref.getLong(TIMESTAMP_ACTIVITY_TRIGGER_KEY, 0)

        return if (timestamp < System.currentTimeMillis() - 20 * 1000) emptySet()
        else set
    }

    private fun getLocationTriggeredRules(): Set<Int> {
        val set = Json.decodeFromString<Set<Int>>(
            sharedPref.getString(LOCATION_TRIGGERED_RUES_KEY, emptySetStr) ?: emptySetStr
        )
        val timestamp = sharedPref.getLong(TIMESTAMP_LOCATION_TRIGGER_KEY, 0)

        return if (timestamp < System.currentTimeMillis() - 20 * 1000) emptySet()
        else set
    }

    private fun updateTimeTriggeredRules(str: String) {
        sharedPref.edit {
            putString(TIME_TRIGGERED_RUES_KEY, str)
            putLong(TIMESTAMP_TIME_TRIGGER_KEY, System.currentTimeMillis())
            commit()
        }
    }

    private fun updateActivityTriggeredRules(str: String) {
        sharedPref.edit {
            putString(ACTIVITY_TRIGGERED_RUES_KEY, str)
            putLong(TIMESTAMP_ACTIVITY_TRIGGER_KEY, System.currentTimeMillis())
            commit()
        }
    }

    private fun updateLocationTriggeredRules(str: String) {
        sharedPref.edit {
            putString(LOCATION_TRIGGERED_RUES_KEY, str)
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
            putString(LOCATION_TRIGGERED_RUES_KEY, Json.encodeToString(ruleSet))
            putLong(TIMESTAMP_RULE_SET_KEY, System.currentTimeMillis())
            commit()
        }
    }

    private suspend fun checkTrigger(
        timeTriggerMap: Set<Int> = getTimeTriggeredRules(),
        activityTriggerMap: Set<Int> = getActivityTriggeredRules(),
        locationTriggerMap: Set<Int> = getLocationTriggeredRules(),
    ) {
        val rules = ruleRepo.getActiveRuleList()
        val ruleSet = getRuleSet()

        val triggeredRules = rules
            .filter { it.timeTrigger == null || it.ruleInfo.ruleId in timeTriggerMap }
            .filter { it.activityTrigger == null || it.ruleInfo.ruleId in activityTriggerMap }
            .filter { it.locationTrigger == null || it.ruleInfo.ruleId in locationTriggerMap }

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
    }

    private fun applyRuleChange(rule: Rule) {
    }

    private fun run(startedRules: List<Rule>, stoppedRules: List<Rule>) {
        /* TODO: Make ActionServices */
        /* TODO: Pass Delta Results so that services can apply the changes */

        /* For Ringer/DND Mode:  */

        stoppedRules.forEach {
            if (it.appBlockAction != null) {
            }
            if (it.notificationAction != null) {
            }
            if (it.dndAction != null) {
            }
            if (it.ringerAction != null) {
            }
        }
    }

    private fun notifyRules(rulesToNotify: List<Rule>, notifiedRules: List<Rule>) {
        /* TODO: Refactor this function */
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

    companion object {
        const val TIME_TRIGGER = "TIME_TRIGGER"
        const val ACTIVITY_TRIGGER = "ACTIVITY_TRIGGER"
        const val LOCATION_TRIGGER = "LOCATION_TRIGGER"

        const val MAIN_SERVICE_PREF_NAME = "com.yeongil.digitalwellbeing.MAIN_SERVICE"

        const val TIMESTAMP_TIME_TRIGGER_KEY = "TIMESTAMP_TIME_TRIGGER"
        const val TIMESTAMP_ACTIVITY_TRIGGER_KEY = "TIMESTAMP_ACTIVITY_TRIGGER"
        const val TIMESTAMP_LOCATION_TRIGGER_KEY = "TIMESTAMP_LOCATION_TRIGGER"

        const val TIME_TRIGGERED_RUES_KEY = "TIME_TRIGGERED_RUES"
        const val ACTIVITY_TRIGGERED_RUES_KEY = "ACTIVITY_TRIGGERED_RUES"
        const val LOCATION_TRIGGERED_RUES_KEY = "LOCATION_TRIGGERED_RUES"

        const val TIMESTAMP_RULE_SET_KEY = "TIMESTAMP_RULE_SET"
        const val RULE_SET_KEY = "RULE_SET"

        val emptySetStr = Json.encodeToString(emptySet<Unit>())
        val ruleSetDefaultStr = Json.encodeToString(RuleSet())
    }
}