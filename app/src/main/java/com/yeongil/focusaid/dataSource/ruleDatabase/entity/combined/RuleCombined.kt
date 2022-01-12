package com.yeongil.focusaid.dataSource.ruleDatabase.entity.combined

import androidx.room.Embedded
import androidx.room.Relation
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.RuleEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.AppBlockActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.DndActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.NotificationActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.RingerActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.ActivityTriggerEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.LocationTriggerEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.TimeTriggerEntity

data class RuleCombined(
    @Embedded
    val ruleEntity: RuleEntity,
    @Relation(parentColumn = "rid", entityColumn = "rid")
    val locationTriggerEntity: LocationTriggerEntity?,
    @Relation(parentColumn = "rid", entityColumn = "rid")
    val timeTriggerEntity: TimeTriggerEntity?,
    @Relation(parentColumn = "rid", entityColumn = "rid")
    val activityTriggerEntity: ActivityTriggerEntity?,
    @Relation(entity = AppBlockActionEntity::class, parentColumn = "rid", entityColumn = "rid")
    val appBlockActionCombined: AppBlockActionCombined?,
    @Relation(entity = NotificationActionEntity::class, parentColumn = "rid", entityColumn = "rid")
    val notificationActionCombined: NotificationActionCombined?,
    @Relation(parentColumn = "rid", entityColumn = "rid")
    val dndActionEntity: DndActionEntity?,
    @Relation(parentColumn = "rid", entityColumn = "rid")
    val ringerActionEntity: RingerActionEntity?,
)