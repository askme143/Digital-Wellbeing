package com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeongil.focusaid.data.rule.trigger.ActivityTrigger
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "activity_triggers")
data class ActivityTriggerEntity(
    @PrimaryKey val rid: Int,
    @Embedded val activityTrigger: ActivityTrigger
)