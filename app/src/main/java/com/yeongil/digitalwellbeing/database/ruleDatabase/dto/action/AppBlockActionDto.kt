package com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "app_block_actions")
data class AppBlockActionDto(
    @PrimaryKey val rid: Int,
    @ColumnInfo(name = "app_block_entry_list") val appBlockEntryList: List<AppBlockEntryDto>,
    @ColumnInfo(name = "handling_action") val handlingAction: Int,    //TODO: handling_action : integer mapping table
)