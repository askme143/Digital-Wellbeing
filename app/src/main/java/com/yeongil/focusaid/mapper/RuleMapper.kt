package com.yeongil.focusaid.mapper

import com.yeongil.focusaid.data.rule.Rule
import com.yeongil.focusaid.data.rule.RuleInfo
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.RuleEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.combined.RuleCombined

fun RuleInfo.toEntity(rid: Int): RuleEntity {
    return RuleEntity(rid, ruleName, activated, notiOnTrigger)
}

fun Rule.toCombined(rid: Int): RuleCombined {
    return RuleCombined(
        ruleInfo.toEntity(rid),
        locationTrigger?.toEntity(rid),
        timeTrigger?.toEntity(rid),
        activityTrigger?.toEntity(rid),
        appBlockAction?.toCombined(rid),
        notificationAction?.toCombined(rid),
        dndAction?.toEntity(rid),
        ringerAction?.toEntity(rid)
    )
}

fun RuleEntity.toData(): RuleInfo {
    return RuleInfo(ruleId, ruleName, activated, notiOnTrigger)
}

fun RuleCombined.toData(): Rule {
    return Rule(
        ruleEntity.toData(),
        locationTriggerEntity?.toData(),
        timeTriggerEntity?.toData(),
        activityTriggerEntity?.toData(),
        appBlockActionCombined?.toData(),
        notificationActionCombined?.toData(),
        dndActionEntity?.toData(),
        ringerActionEntity?.toData()
    )
}