package com.yeongil.digitalwellbeing.data.rule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
data class RuleInfo(
    val ruleId: Int,
    @ColumnInfo(name = "rule_name") val ruleName: String,
    val activated: Boolean,
    @ColumnInfo(name = "noti_on_trigger") val notiOnTrigger: Boolean,
)