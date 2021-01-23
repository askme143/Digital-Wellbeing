package com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action

import kotlinx.serialization.Serializable

@Serializable
data class KeywordEntryDto(
    val keyword: String,
    val inclusion: Boolean,
)