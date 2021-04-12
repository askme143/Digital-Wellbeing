package com.yeongil.focusaid.dataSource.focusAidApi.dto

import kotlinx.serialization.Serializable

@Serializable
data class RuleTriggerLogDto(
    val userName: String,
    val email: String,
    val timestamp: Long,
    val triggered: Boolean,
    val rule: RuleDto
) {
}