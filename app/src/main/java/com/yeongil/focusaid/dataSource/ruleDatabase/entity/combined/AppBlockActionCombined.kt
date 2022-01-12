package com.yeongil.focusaid.dataSource.ruleDatabase.entity.combined

import androidx.room.Embedded
import androidx.room.Relation
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.AppBlockActionEntity

data class AppBlockActionCombined(
    @Embedded
    val appBlockActionEntity: AppBlockActionEntity,
    @Relation(parentColumn = "rid", entityColumn = "rid")
    val appBlockEntryEntityList: List<AppBlockActionEntity.AppBlockEntryEntity>
)