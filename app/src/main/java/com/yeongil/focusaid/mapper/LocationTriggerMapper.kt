package com.yeongil.focusaid.mapper

import com.yeongil.focusaid.data.rule.trigger.LocationTrigger
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.LocationTriggerEntity

fun LocationTrigger.toEntity(rid: Int): LocationTriggerEntity {
    return LocationTriggerEntity(rid, latitude, longitude, range, locationName)
}

fun LocationTriggerEntity.toData(): LocationTrigger {
    return LocationTrigger(latitude, longitude, range, locationName)
}