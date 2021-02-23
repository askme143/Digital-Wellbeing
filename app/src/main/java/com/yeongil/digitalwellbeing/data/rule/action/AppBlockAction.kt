package com.yeongil.digitalwellbeing.data.rule.action

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class AppBlockAction(
    @ColumnInfo(name = "app_block_entry_list") val appBlockEntryList: List<AppBlockEntry>,
    @ColumnInfo(name = "all_app_block") val allAppBlock: Boolean,
    @ColumnInfo(name = "all_app_handling_action") val allAppHandlingAction: Int,
) : Parcelable