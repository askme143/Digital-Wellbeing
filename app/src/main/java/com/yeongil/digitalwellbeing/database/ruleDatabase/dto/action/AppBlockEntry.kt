package com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action

import kotlinx.serialization.Serializable

@Serializable
data class AppBlockEntry(
    val appName: String,
    val allowedTimeInMinutes: Int,
)