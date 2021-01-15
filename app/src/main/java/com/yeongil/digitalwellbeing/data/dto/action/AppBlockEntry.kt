package com.yeongil.digitalwellbeing.data.dto.action

import kotlinx.serialization.Serializable

@Serializable
data class AppBlockEntry(
    val appName: String,
    val allowedTimeInMinutes: Int,
)