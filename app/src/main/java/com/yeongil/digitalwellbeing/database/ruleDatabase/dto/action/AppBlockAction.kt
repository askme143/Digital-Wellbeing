package com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "app_block_actions")
data class AppBlockAction(
    @PrimaryKey val rid: Int,
    @ColumnInfo(name = "app_block_entries") val AppBlockEntries: List<AppBlockEntry>,
    @ColumnInfo(name = "handling_action") val handlingAction: Int,    //TODO: handling_action : integer mapping table
)