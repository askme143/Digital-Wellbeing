package com.yeongil.focusaid.mapper

import com.yeongil.focusaid.data.rule.action.AppBlockAction
import com.yeongil.focusaid.data.rule.action.AppBlockEntry
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.AppBlockActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.AppBlockActionEntity.AppBlockEntryEntity

fun AppBlockAction.toEntity(rid: Int): AppBlockActionEntity {
    return AppBlockActionEntity(
        rid,
        appBlockEntryList.map { it.toEntity() },
        allAppBlock,
        allAppHandlingAction
    )
}

fun AppBlockActionEntity.toData(): AppBlockAction {
    return AppBlockAction(
        appBlockEntryEntityList.map { it.toData() },
        allAppBlock,
        allAppHandlingAction
    )
}

fun AppBlockEntry.toEntity(): AppBlockEntryEntity {
    return AppBlockEntryEntity(packageName, allowedTimeInMinutes, handlingAction)
}

fun AppBlockEntryEntity.toData(): AppBlockEntry {
    return AppBlockEntry(packageName, allowedTimeInMinutes, handlingAction)
}