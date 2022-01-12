package com.yeongil.focusaid.mapper

import com.yeongil.focusaid.data.rule.action.KeywordEntry
import com.yeongil.focusaid.data.rule.action.NotificationAction
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.NotificationActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.NotificationActionEntity.KeywordEntryEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.combined.NotificationActionCombined

fun NotificationAction.toEntity(rid: Int): NotificationActionEntity {
    return NotificationActionEntity(rid, allApp, handlingAction)
}


fun NotificationAction.toCombined(rid: Int): NotificationActionCombined {
    return NotificationActionCombined(
        toEntity(rid),
        keywordList.map { it.toEntity(rid) },
        toPackageNameEntityList(rid)
    )
}

fun NotificationActionCombined.toData(): NotificationAction {
    return NotificationAction(
        packageNameEntityList.map { it.toData() },
        notificationActionEntity.allApp,
        keywordEntryEntityList.map { it.toData() },
        notificationActionEntity.handlingAction
    )
}

fun KeywordEntry.toEntity(rid: Int): KeywordEntryEntity {
    return KeywordEntryEntity(0, rid, keyword, inclusion)
}

fun KeywordEntryEntity.toData(): KeywordEntry {
    return KeywordEntry(keyword, inclusion)
}

fun NotificationActionEntity.PackageNameEntity.toData(): String {
    return packageName
}

fun NotificationAction.toPackageNameEntityList(rid: Int): List<NotificationActionEntity.PackageNameEntity> {
    return appList.map { NotificationActionEntity.PackageNameEntity(0, rid, it) }
}