package com.yeongil.focusaid.dataSource.focusAidApi.dto

data class RuleDeleteLogDto(
    val userName: String,
    val email: String,
    val timestamp: Long,
    val ruleId: Int
) {
}
/*
{
	"userName": "윤영일",
	"email": "askme143@kaist.ac.kr",
	"timestamp": 123,
	"ruleId": 1
}
 */