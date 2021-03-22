package com.yeongil.focusaid.data.rule.action

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class NotificationAction(
    @ColumnInfo(name = "app_list") val appList: List<String>,
    @ColumnInfo(name = "all_app") val allApp: Boolean,
    @ColumnInfo(name = "keyword_list") val keywordList: List<KeywordEntry>,
    @ColumnInfo(name = "handling_action") val handlingAction: Int,
): Parcelable