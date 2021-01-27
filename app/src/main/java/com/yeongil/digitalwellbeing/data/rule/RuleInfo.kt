package com.yeongil.digitalwellbeing.data.rule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeongil.digitalwellbeing.utils.TEMPORAL_RULE_ID
import kotlinx.serialization.Serializable

@Serializable
data class RuleInfo(
    val ruleId: Int = TEMPORAL_RULE_ID,
    @ColumnInfo(name = "rule_name") val ruleName: String = "",
    val activated: Boolean = true,
    @ColumnInfo(name = "noti_on_trigger") val notiOnTrigger: Boolean = false,
)