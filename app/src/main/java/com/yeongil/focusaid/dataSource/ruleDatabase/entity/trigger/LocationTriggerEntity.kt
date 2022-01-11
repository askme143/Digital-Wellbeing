package com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "location_triggers")
data class LocationTriggerEntity(
    @PrimaryKey val rid: Int,
    val latitude: Double,
    val longitude: Double,
    val range: Int,
    @ColumnInfo(name = "location_name") val locationName: String
)