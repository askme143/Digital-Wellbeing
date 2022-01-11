package com.yeongil.focusaid.mapper

import com.yeongil.focusaid.data.rule.trigger.ActivityTrigger
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.ActivityTriggerEntity

fun ActivityTrigger.toEntity(rid: Int): ActivityTriggerEntity {
    return ActivityTriggerEntity(rid, activity)
}

fun ActivityTriggerEntity.toData(): ActivityTrigger {
    return ActivityTrigger(activity)
}