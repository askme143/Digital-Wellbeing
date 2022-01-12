package com.yeongil.focusaid.dataSource.ruleDatabase.entity.combined

import androidx.room.Embedded
import androidx.room.Relation
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.NotificationActionEntity

data class NotificationActionCombined(
    @Embedded
    val notificationActionEntity: NotificationActionEntity,
    @Relation(parentColumn = "rid", entityColumn = "rid")
    val keywordEntryEntityList: List<NotificationActionEntity.KeywordEntryEntity>,
    @Relation(parentColumn = "rid", entityColumn = "rid")
    val packageNameEntityList: List<NotificationActionEntity.PackageNameEntity>
)