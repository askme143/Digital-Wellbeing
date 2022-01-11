package com.yeongil.focusaid.mapper

import com.yeongil.focusaid.data.rule.action.KeywordEntry
import com.yeongil.focusaid.data.rule.action.NotificationAction
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.NotificationActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.NotificationActionEntity.KeywordEntryEntity

fun NotificationAction.toEntity(rid: Int): NotificationActionEntity {
    return NotificationActionEntity(
        rid,
        appList,
        allApp,
        keywordList.map { it.toEntity() },
        handlingAction
    )
}

fun NotificationActionEntity.toData(): NotificationAction {
    return NotificationAction(
        appList,
        allApp,
        keywordEntryEntityList.map { it.toData() },
        handlingAction
    )
}

fun KeywordEntry.toEntity(): KeywordEntryEntity {
    return KeywordEntryEntity(keyword, inclusion)
}

fun KeywordEntryEntity.toData(): KeywordEntry {
    return KeywordEntry(keyword, inclusion)
}