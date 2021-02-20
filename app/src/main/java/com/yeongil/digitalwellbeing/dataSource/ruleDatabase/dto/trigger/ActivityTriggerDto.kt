package com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.trigger

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeongil.digitalwellbeing.data.rule.trigger.ActivityTrigger
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "activity_triggers")
data class ActivityTriggerDto(
    @PrimaryKey val rid: Int,
    @Embedded val activityTrigger: ActivityTrigger
)