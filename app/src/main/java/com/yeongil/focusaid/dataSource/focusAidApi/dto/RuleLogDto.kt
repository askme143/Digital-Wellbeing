package com.yeongil.focusaid.dataSource.focusAidApi.dto

data class RuleLogDto(
    private val userName: String,

    val email: String,

    val timestamp: Long,

    val timeTakenInSeconds: Int,

    val rule: FocusAidRuleDto,
) {}