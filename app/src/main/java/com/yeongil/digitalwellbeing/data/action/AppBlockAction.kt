package com.yeongil.digitalwellbeing.data.action

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class AppBlockAction(
    @ColumnInfo(name = "app_block_entry_list") val appBlockEntryList: List<AppBlockEntry>,
    @ColumnInfo(name = "all_app_block") val allAppBlock: Boolean,
    @ColumnInfo(name = "all_app_handling_action") val allAppHandlingAction: Int,
)