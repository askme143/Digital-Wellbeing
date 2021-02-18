package com.yeongil.digitalwellbeing.dataSource

import android.content.Context
import com.yeongil.digitalwellbeing.data.MainServicePref
import com.yeongil.digitalwellbeing.data.rule.Rule
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainServicePrefSource(context: Context) {
    companion object {
        const val MAIN_SERVICE_NAME = "com.yeongil.digitalwellbeing.MAIN_SERVICE"

        const val TIME_STAMP_KEY = "TIME_STAMP"
        const val CURRENT_ACTIVITIES_KEY = "CURRENT_ACTIVITY"
        const val NOTIFIED_RULES_KEY = "NOTIFIED_RULES"
        const val CONFLICTING_RULES_KEY = "CONFLICTING_RULES"
        const val RUNNING_RULES_KEY = "RUNNING_RULES"

        val mainServiceMutex = Mutex()
    }

    private val sharedPref =
        context.getSharedPreferences(MAIN_SERVICE_NAME, Context.MODE_PRIVATE)

    suspend fun getMainServicePref(): MainServicePref {
        mainServiceMutex.withLock {
            val timeStamp = sharedPref.getLong(TIME_STAMP_KEY, 0)
            val currentActivities = Json.decodeFromString<List<String>>(
                sharedPref.getString(CURRENT_ACTIVITIES_KEY, "[]") ?: "[]"
            )
            val notifiedRules = Json.decodeFromString<List<Rule>>(
                sharedPref.getString(NOTIFIED_RULES_KEY, "[]") ?: "[]"
            )
            val conflictingRules = Json.decodeFromString<List<Rule>>(
                sharedPref.getString(CONFLICTING_RULES_KEY, "[]") ?: "[]"
            )
            val runningRules = Json.decodeFromString<List<Rule>>(
                sharedPref.getString(RUNNING_RULES_KEY, "[]") ?: "[]"
            )

            return MainServicePref(
                timeStamp,
                currentActivities,
                notifiedRules,
                conflictingRules,
                runningRules
            )
        }
    }

    suspend fun updateMainServicePref(pref: MainServicePref) {
        mainServiceMutex.withLock {
            with(sharedPref.edit()) {
                putLong(TIME_STAMP_KEY, pref.timestamp)
                putString(CURRENT_ACTIVITIES_KEY, Json.encodeToString(pref.currentActivities))
                putString(NOTIFIED_RULES_KEY, Json.encodeToString(pref.notifiedRules))
                putString(CONFLICTING_RULES_KEY, Json.encodeToString(pref.conflictingRules))
                putString(RUNNING_RULES_KEY, Json.encodeToString(pref.runningRules))
                commit()
            }
        }
    }

    suspend fun updateCurrentActivities(timestamp: Long, activities: List<String>) {
        mainServiceMutex.withLock {
            with(sharedPref.edit()) {
                putLong(TIME_STAMP_KEY, timestamp)
                putString(CURRENT_ACTIVITIES_KEY, Json.encodeToString(activities))
                commit()
            }
        }
    }
}
