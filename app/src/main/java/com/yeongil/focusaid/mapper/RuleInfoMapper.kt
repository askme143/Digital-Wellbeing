package com.yeongil.focusaid.mapper

import com.yeongil.focusaid.data.rule.RuleInfo
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.RuleInfoEntity

fun RuleInfo.toEntity(): RuleInfoEntity {
    return RuleInfoEntity(ruleId, ruleName, activated, notiOnTrigger)
}

fun RuleInfoEntity.toData(): RuleInfo {
    return RuleInfo(ruleId, ruleName, activated, notiOnTrigger)
}