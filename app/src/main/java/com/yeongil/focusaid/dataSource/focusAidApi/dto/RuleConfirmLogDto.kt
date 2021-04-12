package com.yeongil.focusaid.dataSource.focusAidApi.dto

import kotlinx.serialization.Serializable

enum class Confirmed(val value: String) {
    IGNORED("IGNORED"), REMOVED("REMOVED"), CONFIRMED("CONFIRMED")
}

@Serializable
data class RuleConfirmLogDto(
    val userName: String,
    val email: String,
    val timestamp: Long,
    val confirmed: String,
    val rule: RuleDto
) {
}