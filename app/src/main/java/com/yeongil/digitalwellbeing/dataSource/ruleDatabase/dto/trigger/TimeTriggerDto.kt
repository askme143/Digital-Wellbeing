package com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.trigger

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeongil.digitalwellbeing.data.rule.trigger.TimeTrigger
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "time_triggers")
data class TimeTriggerDto(
    @PrimaryKey val rid: Int,
    @Embedded val timeTrigger: TimeTrigger
)