package com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action

import kotlinx.serialization.Serializable

@Serializable
data class AppBlockEntryDto(
    val appName: String,
    val allowedTimeInMinutes: Int,
)