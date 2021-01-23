package com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action

import kotlinx.serialization.Serializable

@Serializable
data class KeywordEntry(
    val keyword: String,
    val inclusion: Boolean,
)