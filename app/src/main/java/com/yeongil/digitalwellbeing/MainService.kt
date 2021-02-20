package com.yeongil.digitalwellbeing

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.media.AudioManager
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.*
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity.*
import com.google.android.gms.location.DetectedActivity.STILL
import com.google.android.gms.location.LocationServices
import com.yeongil.digitalwellbeing.background.MainNotification
import com.yeongil.digitalwellbeing.data.MainServicePref
import com.yeongil.digitalwellbeing.data.rule.Rule
import com.yeongil.digitalwellbeing.dataSource.MainServicePrefSource
import com.yeongil.digitalwellbeing.dataSource.SequenceNumber
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.RuleDatabase
import com.yeongil.digitalwellbeing.repository.MainServicePrefRepository
import com.yeongil.digitalwellbeing.repository.RuleRepository
import com.yeongil.digitalwellbeing.utils.*
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.*

class MainService : LifecycleService() {
    companion object {
        const val ID = 1
        const val RULE_NOTIFICATION_CHANNEL_ID = "digital_wellbeing_rule_notification_channel"
    }

    /* Every properties are safe after super.onCreate().
    * Some initialization before super.onCreate() may be dangerous. */

    /* System Services */
    private val pendingIntent by lazy {
        Intent(this, MainService::class.java).let { intent ->
            PendingIntent.getService(this, 0, intent, 0)
        }
    }
    private val alarmManager by lazy { getSystemService(Context.ALARM_SERVICE) as AlarmManager }
    private val audioManager by lazy { getSystemService(Context.AUDIO_SERVICE) as AudioManager }
    private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    /* Dependencies */
    private val builder by lazy { MainNotification.getBuilder(this) }
    private val ruleRepo by lazy {
        RuleRepository(
            SequenceNumber(this),
            RuleDatabase.getInstance(this).ruleDao()
        )
    }
    private val prefRepo by lazy {
        MainServicePrefRepository(
            MainServicePrefSource(this)
        )
    }
    private val fusedLocationClient by lazy {
        FusedLocationClient(
            LocationServices.getFusedLocationProviderClient(this)
        )
    }
    private val activityClient by lazy { ActivityRecognition.getClient(this) }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        /* If ActivityRecognition API calls the service */
        val activityResult = ActivityRecognitionResult.extractResult(intent)
        if (activityResult != null) {
            updateCurrentActivities(activityResult)
            return START_STICKY
        }

        loop()
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        init()
    }

    override fun onBind(intent: Intent): IBinder? {
        super.onBind(intent)
        return null
    }

    private fun init() {
        /* Channel for rule execution confirmation */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                RULE_NOTIFICATION_CHANNEL_ID,
                "Digitall Wellbeing Rule Confirm Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Digital Wellbeing Rule Execution Confirm"
            }

            notificationManager.createNotificationChannel(serviceChannel)
        }

        /* Start observing user activity */
        activityClient.requestActivityUpdates(
            5 * 1000,
            Intent(this, MainActivity::class.java).let { intent ->
                PendingIntent.getService(this, 0, intent, 0)
            })

        /* Indicate that the foreground service is created is this call */
        startForeground(ID, builder.build())
    }

    private fun updateCurrentActivities(activityResult: ActivityRecognitionResult) {
        val currentActivities = activityResult.probableActivities
            .filter { it.confidence > 30 }
            .filter { it.type in listOf(IN_VEHICLE, ON_BICYCLE, STILL) }
            .map {
                when (it.type) {
                    IN_VEHICLE -> DRIVE
                    ON_BICYCLE -> BICYCLE
                    STILL -> com.yeongil.digitalwellbeing.utils.STILL
                    else -> "other"
                }
            }
        val timestamp = System.currentTimeMillis()

        lifecycleScope.launch(Dispatchers.Default) {
            prefRepo.updateCurrentActivities(timestamp, currentActivities)
        }
    }

    private fun confirmNotifiedRule() {
        /* TODO: Check validity of the rule. Update NotifiedRules and RunningRules. */
    }

    private fun loop() {
        lifecycleScope.launch(Dispatchers.Default) {
            val pref = prefRepo.getPreference()
            val triggeredRules =
                getTriggeredRules(ruleRepo.getActiveRuleList(), pref.currentActivities)

            val rulesToNotify = triggeredRules
                .filter { it.ruleInfo.notiOnTrigger }
                .minus(pref.conflictingRules)
                .minus(pref.runningRules)
            val rulesToRun =
                handleConflict(
                    triggeredRules.filter { !it.ruleInfo.notiOnTrigger },
                    pref.conflictingRules,
                    pref.runningRules
                )
            val rulesToBeConflicted = triggeredRules - rulesToNotify - rulesToRun

            notifyRules(rulesToNotify, pref.notifiedRules)
            runRules(rulesToRun, pref.runningRules)

            val timestamp = System.currentTimeMillis()
            prefRepo.updatePreference(
                MainServicePref(
                    timestamp,
                    pref.currentActivities,
                    rulesToNotify,
                    rulesToBeConflicted,
                    rulesToRun
                )
            )
        }

        /* Recursive call */
        alarmManager.cancel(pendingIntent)
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 1000 * 10,
            pendingIntent
        )
    }

    private suspend fun getTriggeredRules(
        rules: List<Rule>,
        currentActivities: List<String>
    ): List<Rule> {
        fun timeFilter(list: List<Rule>): List<Rule> {
            val calendar = Calendar.getInstance()

            val day = calendar.get(Calendar.DAY_OF_WEEK) - 1
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val minuteOfDay = hour * 60 + minute

            return list.filter {
                with(it.timeTrigger) {
                    this == null ||
                            repeatDay[day] && minuteOfDay in startTimeInMinutes until endTimeInMinutes
                }
            }
        }

        fun activityFilter(list: List<Rule>, currentActivities: List<String>): List<Rule> {
            return list.filter {
                with(it.activityTrigger) { this == null || activity in currentActivities }
            }
        }

        suspend fun locationFilter(list: List<Rule>): List<Rule> {
            val triggerList = list.map { it.locationTrigger }
            if (triggerList.all { it == null }) return list

            val location = try {
                fusedLocationClient.getLastLocation()
            } catch (error: Exception) {
                return listOf()
            }

            val currLocation = Location("curr location")
            currLocation.latitude = location.latitude
            currLocation.longitude = location.longitude

            return list.filter {
                with(it.locationTrigger) {
                    this == null || run {
                        val dest = Location("dest location").also { dest ->
                            dest.latitude = latitude
                            dest.longitude = longitude
                        }
                        currLocation.distanceTo(dest) < range
                    }
                }
            }
        }

        return locationFilter(activityFilter(timeFilter(rules), currentActivities))
    }

    /* Remove conflicts and return rules to run */
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
        val oldRules = rules - newRules

        val newRulesToRun = removeConflictInNewRules(newRules)
        val oldRulesToRun = runningRules
            .filter { it in oldRules }
            .let { removeConflictInOldRules(newRulesToRun, it) }

        return newRulesToRun + oldRulesToRun
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

    private fun runRules(rulesToRun: List<Rule>, runningRules: List<Rule>) {
        /* TODO: Run Rules */
    }
}