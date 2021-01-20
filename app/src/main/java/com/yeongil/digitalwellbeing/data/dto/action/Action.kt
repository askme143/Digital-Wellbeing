package com.yeongil.digitalwellbeing.data.dto.action

import androidx.room.Embedded
import androidx.room.Relation
import com.yeongil.digitalwellbeing.data.dto.rule.RuleInfo
import kotlinx.serialization.Serializable

@Serializable
class Action (
    @Embedded
    val ruleInfo: RuleInfo,
    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val appBlockAction: AppBlockAction?,
    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val notificationAction: NotificationAction?,
    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val dndAction: DndAction?,
    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val ringerAction: RingerAction?,
)