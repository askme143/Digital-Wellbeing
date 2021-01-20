package com.yeongil.digitalwellbeing.data.dto.trigger

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "time_triggers")
data class TimeTrigger(
    @PrimaryKey val rid: Int,
    @ColumnInfo(name="start_time_in_minutes") val startTimeInMinutes: Int,
    @ColumnInfo(name="end_time_in_minutes") val endTimeInMinutes: Int,
    @ColumnInfo(name="repeat_day") val repeatDay: List<Boolean>,
)