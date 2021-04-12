package com.yeongil.focusaid.dataSource.focusAidApi.dto

import kotlinx.serialization.Serializable

@Serializable
data class RuleConfirmLogDto(
    val userName: String,
    val email: String,
    val timestamp: Long,
    val confirmed: Boolean,
    val rule: RuleDto
) {
}