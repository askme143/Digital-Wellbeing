package com.yeongil.digitalwellbeing.data.action

import kotlinx.serialization.Serializable

@Serializable
data class KeywordEntry(
    val keyword: String,
    val inclusion: Boolean,
)