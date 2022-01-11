package com.yeongil.focusaid.dataSource.ruleDatabase.entity.action

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "notification_actions")
data class NotificationActionEntity(
    @PrimaryKey val rid: Int,
    @ColumnInfo(name = "app_list") val appList: List<String>,
    @ColumnInfo(name = "all_app") val allApp: Boolean,
    @ColumnInfo(name = "keyword_list") val keywordEntryEntityList: List<KeywordEntryEntity>,
    @ColumnInfo(name = "handling_action") val handlingAction: Int,
) {
    @Serializable
    @Parcelize
    data class KeywordEntryEntity(
        val keyword: String,
        val inclusion: Boolean,
    ) : Parcelable
}