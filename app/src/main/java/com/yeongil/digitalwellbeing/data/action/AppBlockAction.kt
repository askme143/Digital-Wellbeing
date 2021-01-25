package com.yeongil.digitalwellbeing.data.action

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class AppBlockAction(
    @ColumnInfo(name = "app_block_entry_list") val appBlockEntryList: List<AppBlockEntry>,
)