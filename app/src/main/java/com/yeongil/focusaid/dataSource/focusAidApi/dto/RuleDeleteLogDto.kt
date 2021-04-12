package com.yeongil.focusaid.dataSource.focusAidApi.dto

import kotlinx.serialization.Serializable

@Serializable
data class RuleDeleteLogDto(
    val userName: String,
    val email: String,
    val timestamp: Long,
    val ruleId: Int
) {
}