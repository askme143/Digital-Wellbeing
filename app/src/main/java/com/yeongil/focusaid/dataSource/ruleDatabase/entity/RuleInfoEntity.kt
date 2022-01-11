package com.yeongil.focusaid.dataSource.ruleDatabase.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.yeongil.focusaid.data.rule.RuleInfo
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "rule_info",
    indices = [Index(value = ["rule_name"], unique = true)]
)
data class RuleInfoEntity(
    @PrimaryKey val rid: Int,
    @Embedded val ruleInfo: RuleInfo,
)