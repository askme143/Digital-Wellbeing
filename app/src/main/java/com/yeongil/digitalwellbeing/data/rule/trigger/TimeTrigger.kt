package com.yeongil.digitalwellbeing.data.rule.trigger

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class TimeTrigger(
    @ColumnInfo(name="start_time_in_minutes") val startTimeInMinutes: Int,
    @ColumnInfo(name="end_time_in_minutes") val endTimeInMinutes: Int,
    @ColumnInfo(name="repeat_day") val repeatDay: List<Boolean>,
) : Parcelable