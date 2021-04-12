package com.yeongil.focusaid.dataSource.logDatabase.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "rule_trigger_log")
data class RuleTriggerLogDto(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val log: String,
) {
}