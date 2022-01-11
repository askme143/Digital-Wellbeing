package com.yeongil.focusaid.mapper

import com.yeongil.focusaid.data.rule.action.DndAction
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.DndActionEntity

fun DndAction.toEntity(rid: Int): DndActionEntity {
    return DndActionEntity(rid)
}

fun DndActionEntity.toData(): DndAction {
    return DndAction()
}