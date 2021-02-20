package com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.action

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeongil.digitalwellbeing.data.rule.action.AppBlockAction
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "app_block_actions")
data class AppBlockActionDto(
    @PrimaryKey val rid: Int,
    @Embedded val appBlockAction: AppBlockAction
)