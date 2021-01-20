package com.yeongil.digitalwellbeing.data.dto.trigger

import androidx.room.Embedded
import androidx.room.Relation
import com.yeongil.digitalwellbeing.data.dto.rule.RuleInfo
import kotlinx.serialization.Serializable

@Serializable
class Trigger(
    @Embedded val ruleInfo: RuleInfo,
    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val locationTrigger: LocationTrigger?,
    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val timeTrigger: TimeTrigger?,
    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val activityTrigger: ActivityTrigger?,
)