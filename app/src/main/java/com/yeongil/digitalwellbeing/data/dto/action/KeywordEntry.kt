package com.yeongil.digitalwellbeing.data.dto.action

import kotlinx.serialization.Serializable

@Serializable
data class KeywordEntry(
    val keyword: String,
    val inclusion: Boolean,
)