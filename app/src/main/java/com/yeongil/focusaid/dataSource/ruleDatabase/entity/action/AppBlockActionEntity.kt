package com.yeongil.focusaid.dataSource.ruleDatabase.entity.action

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeongil.focusaid.data.rule.action.AppBlockAction
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "app_block_actions")
data class AppBlockActionEntity(
    @PrimaryKey val rid: Int,
    @Embedded val appBlockAction: AppBlockAction
)