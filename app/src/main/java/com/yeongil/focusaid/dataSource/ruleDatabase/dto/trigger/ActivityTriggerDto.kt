package com.yeongil.focusaid.dataSource.ruleDatabase.dto.trigger

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeongil.focusaid.data.rule.trigger.ActivityTrigger
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "activity_triggers")
data class ActivityTriggerDto(
    @PrimaryKey val rid: Int,
    @Embedded val activityTrigger: ActivityTrigger
)