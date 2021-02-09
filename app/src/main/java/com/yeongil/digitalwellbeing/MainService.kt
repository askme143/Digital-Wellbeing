package com.yeongil.digitalwellbeing

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.os.SystemClock
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity.*
import com.google.android.gms.location.LocationServices
import com.yeongil.digitalwellbeing.background.MainNotification
import com.yeongil.digitalwellbeing.data.rule.Rule
import com.yeongil.digitalwellbeing.dataSource.SequenceNumber
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.RuleDatabase
import com.yeongil.digitalwellbeing.repository.RuleRepository
import com.yeongil.digitalwellbeing.utils.BICYCLE
import com.yeongil.digitalwellbeing.utils.DRIVE
import com.yeongil.digitalwellbeing.utils.FusedLocationClient
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import java.lang.Exception
import java.util.*

class MainService : LifecycleService() {
    companion object {
        const val ID = 1
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
    private val vibrator by lazy { getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }

    /* Dependencies */
    private var _builder: NotificationCompat.Builder? = null
    private val builder get() = _builder!!
    private val ruleRepo by lazy {
        RuleRepository(
            SequenceNumber(this),
            RuleDatabase.getInstance(this).ruleDao()
        )
    }
    private val fusedLocationClient by lazy {
        FusedLocationClient(
            LocationServices.getFusedLocationProviderClient(this)
        )
    }
    private val activityClient by lazy { ActivityRecognition.getClient(this) }

    /* Data */
    private val ruleList by lazy {
        ruleRepo.getActiveRuleListAsFlow().asLiveData()
    }
    private var currentActivities = listOf<String>()
    private val triggeredRules = MutableLiveData<List<Rule>>()


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        init()

        /* If ActivityRecognition API calls the service */
        val activityResult = ActivityRecognitionResult.extractResult(intent)
        if (activityResult != null) {
            currentActivities = activityResult.probableActivities
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
        /* If the service is not initialized */
        if (_builder == null) {
            _builder = MainNotification.getBuilder(this)

            /* Start observing live data */
            ruleList.observe(this, Observer {})

            /* Start observing user activity */
            activityClient.requestActivityUpdates(5 * 1000, pendingIntent)
        }

        startForeground(ID, builder.build())
    }

    private fun loop() {
        lifecycleScope.launch { checkTrigger() }

        /* Recursive call */
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            alarmManager.cancel(pendingIntent)
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 1000 * 10,
                pendingIntent
            )
        }
    }

    private suspend fun checkTrigger() {
        val list = ruleList.value ?: listOf()
        vibrate()

        triggeredRules.value = locationFilter(activityFilter(timeFilter(list)))
        builder.setContentText("${triggeredRules.value!!.size} 개")
        startForeground(ID, builder.build())
    }

    private fun timeFilter(list: List<Rule>): List<Rule> {
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

    private fun activityFilter(list: List<Rule>): List<Rule> {
        return list.filter {
            with(it.activityTrigger) { this == null || activity in currentActivities }
        }
    }

    private suspend fun locationFilter(list: List<Rule>): List<Rule> {
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

    private fun vibrate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    1000,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else vibrator.vibrate(1000)
    }
}