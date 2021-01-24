package com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.trigger

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeongil.digitalwellbeing.data.trigger.LocationTrigger
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "location_triggers")
data class LocationTriggerDto(
    @PrimaryKey val rid: Int,
    @Embedded val locationTrigger: LocationTrigger
)