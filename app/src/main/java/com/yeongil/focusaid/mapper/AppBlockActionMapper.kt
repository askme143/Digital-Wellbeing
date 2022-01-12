package com.yeongil.focusaid.mapper

import com.yeongil.focusaid.data.rule.action.AppBlockAction
import com.yeongil.focusaid.data.rule.action.AppBlockEntry
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.AppBlockActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.AppBlockActionEntity.AppBlockEntryEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.combined.AppBlockActionCombined

fun AppBlockAction.toEntity(rid: Int): AppBlockActionEntity {
    return AppBlockActionEntity(rid, allAppBlock, allAppHandlingAction)
}

fun AppBlockAction.toCombined(rid: Int): AppBlockActionCombined {
    return AppBlockActionCombined(toEntity(rid), appBlockEntryList.map { it.toEntity(rid) })
}

fun AppBlockActionCombined.toData(): AppBlockAction {
    return AppBlockAction(
        appBlockEntryEntityList.map { it.toData() },
        appBlockActionEntity.allAppBlock,
        appBlockActionEntity.allAppHandlingAction
    )
}

fun AppBlockEntry.toEntity(rid: Int): AppBlockEntryEntity {
    return AppBlockEntryEntity(0, rid, packageName, allowedTimeInMinutes, handlingAction)
}

fun AppBlockEntryEntity.toData(): AppBlockEntry {
    return AppBlockEntry(packageName, allowedTimeInMinutes, handlingAction)
}