package com.yeongil.focusaid.dataSource.ruleDatabase.dto.trigger

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeongil.focusaid.data.rule.trigger.TimeTrigger
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "time_triggers")
data class TimeTriggerDto(
    @PrimaryKey val rid: Int,
    @Embedded val timeTrigger: TimeTrigger
)