package com.yeongil.focusaid.background

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.DetectedActivity
import com.yeongil.focusaid.MainActivity
import com.yeongil.focusaid.dataSource.SequenceNumber
import com.yeongil.focusaid.dataSource.ruleDatabase.RuleDatabase
import com.yeongil.focusaid.repository.RuleRepository
import com.yeongil.focusaid.utils.BICYCLE
import com.yeongil.focusaid.utils.DRIVE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ActivityTriggerService : LifecycleService() {
    private val ruleRepo by lazy {
        RuleRepository(
            SequenceNumber(this),
            RuleDatabase.getInstance(this).ruleDao()
        )
    }
    private val sharedPref by lazy {
        getSharedPreferences(ACTIVITY_SERVICE_PREF_NAME, MODE_PRIVATE)
    }
    private val activityClient by lazy { ActivityRecognition.getClient(this) }
    private val pendingIntent by lazy {
        Intent(this, ActivityTriggerService::class.java).let {
            PendingIntent.getService(this, 0, it, 0)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        lifecycleScope.launch(Dispatchers.Default) {
            val rules = ruleRepo.getActiveRuleList()
            val activityRules = rules.filter { it.activityTrigger != null }
            if (activityRules.isEmpty())
                activityClient.removeActivityUpdates(pendingIntent)
            else
                activityClient.requestActivityUpdates(5 * 1000, pendingIntent)

            val activityResult =
                if (intent != null) ActivityRecognitionResult.extractResult(intent) else null
            val currActivities =
                if (activityResult != null) {
                    getActivitiesFromResult(activityResult).also { updateCurrentActivities(it) }
                } else getCurrentActivities()

            val triggeredSet = activityRules
                .filter { it.activityTrigger!!.activity in currActivities }
                .map { it.ruleInfo.ruleId }

            /* Pass results to MainService */
            val mainIntent = Intent(this@ActivityTriggerService, MainService::class.java)
            val triggeredSetStr = Json.encodeToString(triggeredSet)
            mainIntent.putExtra(MainService.ACTIVITY_TRIGGERED_RULES_KEY, triggeredSetStr)
            mainIntent.action = MainService.ACTIVITY_TRIGGER
            startService(mainIntent)

            stopSelf(startId)
        }

        return START_STICKY
    }

    private fun getActivitiesFromResult(activityResult: ActivityRecognitionResult): List<String> {
        return activityResult.probableActivities
            .filter { it.confidence > 30 }
            .filter {
                it.type in listOf(
                    DetectedActivity.IN_VEHICLE,
                    DetectedActivity.ON_BICYCLE,
                    DetectedActivity.STILL
                )
            }
            .map {
                when (it.type) {
                    DetectedActivity.IN_VEHICLE -> DRIVE
                    DetectedActivity.ON_BICYCLE -> BICYCLE
                    DetectedActivity.STILL -> com.yeongil.focusaid.utils.STILL
                    else -> "other"
                }
            }
    }

    private fun getCurrentActivities(): List<String> {
        val list = Json.decodeFromString<List<String>>(
            sharedPref.getString(CURRENT_ACTIVITIES_KEY, emptyListStr) ?: emptyListStr
        )
        val timestamp = sharedPref.getLong(TIMESTAMP_CURRENT_ACTIVITIES_KEY, 0)

        return if (timestamp < System.currentTimeMillis() - 20 * 1000) emptyList()
        else list
    }

    private fun updateCurrentActivities(currentActivities: List<String>) {
        val currentActivitiesStr = currentActivities.let { Json.encodeToString(it) }
        val timestamp = System.currentTimeMillis()

        sharedPref.edit {
            putString(CURRENT_ACTIVITIES_KEY, currentActivitiesStr)
            putLong(TIMESTAMP_CURRENT_ACTIVITIES_KEY, timestamp)
            commit()
        }
    }

    companion object {
        const val ACTIVITY_SERVICE_PREF_NAME = "com.yeongil.focusaid.ACTIVITY_SERVICE"

        const val TIMESTAMP_CURRENT_ACTIVITIES_KEY = "TIMESTAMP_CURRENT_ACTIVITIES"
        const val CURRENT_ACTIVITIES_KEY = "CURRENT_ACTIVITIES"

        val emptyListStr = Json.encodeToString(emptyList<Unit>())
    }
}