package com.yeongil.focusaid.dataSource.logDatabase.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "rule_delete_log")
data class RuleDeleteLogDto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val log: String,
)