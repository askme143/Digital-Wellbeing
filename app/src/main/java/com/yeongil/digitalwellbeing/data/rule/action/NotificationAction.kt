package com.yeongil.digitalwellbeing.data.rule.action

import androidx.room.ColumnInfo
import kotlinx.serialization.Serializable

@Serializable
data class NotificationAction(
    @ColumnInfo(name = "app_list") val appList: List<String>,
    @ColumnInfo(name = "all_app") val allApp: Boolean,
    @ColumnInfo(name = "keyword_list") val keywordList: List<KeywordEntry>,
    @ColumnInfo(name = "handling_action") val handlingAction: Int,
)