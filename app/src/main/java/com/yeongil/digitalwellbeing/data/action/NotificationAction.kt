package com.yeongil.digitalwellbeing.data.action

import kotlinx.serialization.Serializable

@Serializable
data class NotificationAction(
    val appList: List<String>,
    val keywordList: List<KeywordEntry>,
    val handlingAction: Int,
)