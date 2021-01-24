package com.yeongil.digitalwellbeing.data.action

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class AppBlockEntry(
    val appName: String,
    @ColumnInfo (name = "allowed_time_in_minutes") val allowedTimeInMinutes: Int,
)