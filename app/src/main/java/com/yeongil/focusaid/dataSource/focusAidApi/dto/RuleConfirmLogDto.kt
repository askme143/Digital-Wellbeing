package com.yeongil.focusaid.dataSource.focusAidApi.dto

data class RuleConfirmLogDto(
    val userName: String,
    val email: String,
    val timestamp: Long,
    val confirmed: Boolean,
    val rule: FocusAidRuleDto
) {
}