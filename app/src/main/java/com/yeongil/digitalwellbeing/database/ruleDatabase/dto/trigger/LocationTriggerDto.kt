package com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "location_triggers")
data class LocationTriggerDto(
    @PrimaryKey val rid: Int,
    val latitude: Double,
    val longitude: Double,
    val range: Int,
    val locationName: String
)