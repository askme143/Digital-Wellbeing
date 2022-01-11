package com.yeongil.focusaid.mapper

import com.yeongil.focusaid.data.rule.action.RingerAction
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.RingerActionEntity

fun RingerAction.toEntity(rid: Int): RingerActionEntity {
    return RingerActionEntity(rid, ringerMode.toEntity())
}

fun RingerActionEntity.toData(): RingerAction {
    return RingerAction(ringerModeEntity.toData())
}

fun RingerAction.RingerMode.toEntity(): RingerActionEntity.RingerModeEntity {
    return RingerActionEntity.RingerModeEntity.valueOf(name)
}

fun RingerActionEntity.RingerModeEntity.toData(): RingerAction.RingerMode {
    return RingerAction.RingerMode.valueOf(name)
}