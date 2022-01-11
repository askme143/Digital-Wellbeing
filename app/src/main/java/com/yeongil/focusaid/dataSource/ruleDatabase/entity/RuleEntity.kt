package com.yeongil.focusaid.dataSource.ruleDatabase.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.AppBlockActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.DndActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.NotificationActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.RingerActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.ActivityTriggerEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.LocationTriggerEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.TimeTriggerEntity
import kotlinx.serialization.Serializable

@Serializable
data class RuleEntity(
    @Embedded val ruleInfoEntity: RuleInfoEntity,

    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val locationTriggerEntity: LocationTriggerEntity?,

    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val timeTriggerEntity: TimeTriggerEntity?,

    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val activityTriggerEntity: ActivityTriggerEntity?,

    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val appBlockActionEntity: AppBlockActionEntity?,

    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val notificationActionEntity: NotificationActionEntity?,

    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val dndActionEntity: DndActionEntity?,

    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val ringerActionEntity: RingerActionEntity?,
)