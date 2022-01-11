package com.yeongil.focusaid.data.rule.action

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class NotificationAction(
    val appList: List<String>,
    val allApp: Boolean,
    val keywordList: List<KeywordEntry>,
    val handlingAction: Int,
) : Parcelable