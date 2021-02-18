package com.yeongil.digitalwellbeing.repository

import com.yeongil.digitalwellbeing.data.MainServicePref
import com.yeongil.digitalwellbeing.dataSource.MainServicePrefSource

class MainServicePrefRepository(
    private val prefSrc: MainServicePrefSource
) {
    suspend fun getPreference(): MainServicePref {
        val prefFromSrc = prefSrc.getMainServicePref()
        val timeStamp = System.currentTimeMillis()

        return if (prefFromSrc.timestamp < timeStamp - 60 * 1000) {
            MainServicePref(timeStamp)
        } else
            prefFromSrc
    }

    suspend fun updatePreference(pref: MainServicePref) {
        prefSrc.updateMainServicePref(pref)
    }

    suspend fun updateCurrentActivities(timestamp: Long, activities: List<String>) {
        prefSrc.updateCurrentActivities(timestamp, activities)
    }
}