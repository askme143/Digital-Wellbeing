package com.yeongil.digitalwellbeing.data.trigger

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class TimeTrigger(
    @ColumnInfo(name="start_time_in_minutes") val startTimeInMinutes: Int,
    @ColumnInfo(name="end_time_in_minutes") val endTimeInMinutes: Int,
    @ColumnInfo(name="repeat_day") val repeatDay: List<Boolean>,
)