package com.yeongil.focusaid.dataSource.focusAidApi.dto

import kotlinx.serialization.Serializable

@Serializable
data class RuleActivationLogDto(
    val userName: String,
    val email: String,
    val timestamp: Long,
    val rule: RuleDto
) {
}