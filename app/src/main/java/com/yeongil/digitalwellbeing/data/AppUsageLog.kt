package com.yeongil.digitalwellbeing.data

data class AppUsageLog(
    val packageName: String,
    val timestamp: Long,
    val start: Boolean
)