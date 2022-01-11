package com.yeongil.focusaid.dataSource.ruleDatabase.entity.action

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "app_block_actions")
data class AppBlockActionEntity(
    @PrimaryKey val rid: Int,
    @ColumnInfo(name = "app_block_entry_list") val appBlockEntryEntityList: List<AppBlockEntryEntity>,
    @ColumnInfo(name = "all_app_block") val allAppBlock: Boolean,
    @ColumnInfo(name = "all_app_handling_action") val allAppHandlingAction: Int,
) {
    @Serializable
    @Parcelize
    data class AppBlockEntryEntity(
        @ColumnInfo(name = "package_name") val packageName: String,
        @ColumnInfo(name = "allowed_time_in_minutes") val allowedTimeInMinutes: Int,
        @ColumnInfo(name = "handling_action") val handlingAction: Int,
    ) : Parcelable
}