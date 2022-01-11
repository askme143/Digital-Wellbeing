package com.yeongil.focusaid.mapper

import com.yeongil.focusaid.data.rule.Rule
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.RuleEntity

fun Rule.toEntity(rid: Int): RuleEntity {
    return RuleEntity(
        ruleInfo.toEntity(),
        locationTrigger?.toEntity(rid),
        timeTrigger?.toEntity(rid),
        activityTrigger?.toEntity(rid),
        appBlockAction?.toEntity(rid),
        notificationAction?.toEntity(rid),
        dndAction?.toEntity(rid),
        ringerAction?.toEntity(rid)
    )
}

fun RuleEntity.toData(): Rule {
    return Rule(
        ruleInfoEntity.toData(),
        locationTriggerEntity?.toData(),
        timeTriggerEntity?.toData(),
        activityTriggerEntity?.toData(),
        appBlockActionEntity?.toData(),
        notificationActionEntity?.toData(),
        dndActionEntity?.toData(),
        ringerActionEntity?.toData()
    )
}