package com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.RuleEntity

@Entity(
    tableName = "location_trigger",
    foreignKeys = [ForeignKey(
        entity = RuleEntity::class,
        parentColumns = ["rid"],
        childColumns = ["rid"],
        onDelete = CASCADE,
    )]
)
data class LocationTriggerEntity(
    @PrimaryKey val rid: Int,
    val latitude: Double,
    val longitude: Double,
    val range: Int,
    @ColumnInfo(name = "location_name") val locationName: String
)