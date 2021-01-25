package com.yeongil.digitalwellbeing.data.action

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class AppBlockEntry(
    @ColumnInfo(name = "package_name") val packageName: String,
    @ColumnInfo(name = "allowed_time_in_minutes") val allowedTimeInMinutes: Int,
    @ColumnInfo(name = "handling_action") val handlingAction: Int,
)