package com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "activity_triggers")
data class ActivityTriggerEntity(
    @PrimaryKey val rid: Int,
    val activity: String
)