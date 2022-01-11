package com.yeongil.focusaid.mapper

import com.yeongil.focusaid.data.rule.trigger.TimeTrigger
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.TimeTriggerEntity

fun TimeTrigger.toEntity(rid: Int): TimeTriggerEntity {
    return TimeTriggerEntity(rid, startTimeInMinutes, endTimeInMinutes, repeatDay)
}

fun TimeTriggerEntity.toData(): TimeTrigger {
    return TimeTrigger(startTimeInMinutes, endTimeInMinutes, repeatDay)
}