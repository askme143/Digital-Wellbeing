package com.yeongil.digitalwellbeing.data.dto.trigger

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "location_triggers")
data class LocationTrigger(
    @PrimaryKey val rid: Int,
    val latitude: Double,
    val longitude: Double,
    val range: Int,
    val locationName: String
)