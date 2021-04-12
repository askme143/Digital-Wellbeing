package com.yeongil.focusaid.dataSource.logDatabase.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "rule_activation_log")
data class RuleActivationLogDto(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val log: String,
) {
}