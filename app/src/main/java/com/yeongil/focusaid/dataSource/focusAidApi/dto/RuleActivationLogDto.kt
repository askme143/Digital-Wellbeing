package com.yeongil.focusaid.dataSource.focusAidApi.dto

data class RuleActivationLogDto(
    val userName: String,
    val email: String,
    val timestamp: Long,
    val rule: FocusAidRuleDto
) {
}