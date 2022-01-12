package com.yeongil.focusaid.dataSource.ruleDatabase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.yeongil.focusaid.utils.TEMPORAL_RULE_ID
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "rule",
    indices = [Index(value = ["rule_name"], unique = true)]
)
data class RuleEntity(
    @PrimaryKey
    @ColumnInfo(name = "rid")
    val ruleId: Int = TEMPORAL_RULE_ID,
    @ColumnInfo(name = "rule_name")
    val ruleName: String = "",
    @ColumnInfo(name = "activated")
    val activated: Boolean = true,
    @ColumnInfo(name = "noti_on_trigger")
    val notiOnTrigger: Boolean = false,
)