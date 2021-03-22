package com.yeongil.focusaid.dataSource.ruleDatabase.dto

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeongil.focusaid.data.rule.RuleInfo
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "rule_info")
data class RuleInfoDto(
    @PrimaryKey val rid: Int,
    @Embedded val ruleInfo: RuleInfo,
)