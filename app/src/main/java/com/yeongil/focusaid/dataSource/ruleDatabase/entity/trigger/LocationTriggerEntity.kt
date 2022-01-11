package com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeongil.focusaid.data.rule.trigger.LocationTrigger
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "location_triggers")
data class LocationTriggerEntity(
    @PrimaryKey val rid: Int,
    @Embedded val locationTrigger: LocationTrigger
)