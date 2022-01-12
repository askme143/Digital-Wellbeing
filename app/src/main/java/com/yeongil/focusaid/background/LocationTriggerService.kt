package com.yeongil.focusaid.background

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.SystemClock
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.LocationServices
import com.yeongil.focusaid.background.MainService.Companion.LOCATION_TRIGGERED_RULES_KEY
import com.yeongil.focusaid.data.rule.Rule
import com.yeongil.focusaid.dataSource.SequenceNumber
import com.yeongil.focusaid.dataSource.ruleDatabase.RuleDatabase
import com.yeongil.focusaid.repository.RuleRepository
import com.yeongil.focusaid.utils.FusedLocationClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max

class LocationTriggerService : LifecycleService() {
    private val ruleRepo by lazy {
        RuleRepository(SequenceNumber(this), RuleDatabase.getInstance(this).ruleCombinedDao())
    }
    private val fusedLocationClient by lazy {
        FusedLocationClient(LocationServices.getFusedLocationProviderClient(this))
    }
    private val alarmManager by lazy { getSystemService(Context.ALARM_SERVICE) as AlarmManager }
    private val pendingIntent by lazy {
        Intent(this, LocationTriggerService::class.java).let { intent ->
            PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        lifecycleScope.launch(Dispatchers.Default) {
            val rules = ruleRepo.getActiveRuleList()

            val (triggeredSet, minDist) = checkLocationRules(rules)

            /* Set alarm manager */
            if (minDist == null) {
                alarmManager.cancel(pendingIntent)
            } else {
                /* Interval: max (minDist / 80km/h, 10sec) */
                /* MinDist: meters, Default velocity: 80km/h, Interval: ms (Long) */
                val interval = max(ceil(minDist * 60 * 60 / (80)).toLong(), 10 * 1000)

                alarmManager.cancel(pendingIntent)
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + interval,
                    pendingIntent
                )
            }

            /* Pass results to MainService */
            val mainIntent = Intent(this@LocationTriggerService, MainService::class.java)
            val triggeredSetStr = Json.encodeToString(triggeredSet)
            mainIntent.putExtra(LOCATION_TRIGGERED_RULES_KEY, triggeredSetStr)
            mainIntent.action = MainService.LOCATION_TRIGGER
            startService(mainIntent)

            stopSelf(startId)
        }

        return START_STICKY
    }

    private suspend fun checkLocationRules(rules: List<Rule>): Pair<Set<Int>, Float?> {
        val locationRules = rules.filter { it.locationTrigger != null }
        if (locationRules.isEmpty()) return Pair(setOf(), null)

        val location = try {
            fusedLocationClient.getLastLocation() ?: return Pair(setOf(), 0f)
        } catch (error: Exception) {
            return Pair(setOf(), 0f)
        }

        val currLocation = Location("curr location")
        currLocation.latitude = location.latitude
        currLocation.longitude = location.longitude

        val distances = locationRules.map {
            with(it.locationTrigger!!) {
                val dest = Location("dest location").also { dest ->
                    dest.latitude = latitude
                    dest.longitude = longitude
                }
                Pair(it.ruleInfo.ruleId, range - currLocation.distanceTo(dest))
            }
        }

        val triggeredSet = distances.filter { it.second > 0 }.map { it.first }.toSet()
        val minDist = distances.map { abs(it.second) }.minOrNull() ?: 0f

        return Pair(triggeredSet, minDist)
    }
}