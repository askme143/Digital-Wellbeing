package com.yeongil.digitalwellbeing.data.rule.action

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
enum class RingerMode { VIBRATE, RING, SILENT }

@Serializable
data class RingerAction(
    @ColumnInfo(name = "ringer_mode") val ringerMode: RingerMode,
)