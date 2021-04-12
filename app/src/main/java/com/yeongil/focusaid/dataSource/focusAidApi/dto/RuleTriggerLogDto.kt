package com.yeongil.focusaid.dataSource.focusAidApi.dto

data class RuleTriggerLogDto(
    val userName: String,
    val email: String,
    val timestamp: Long,
    val triggered: Boolean,
    val rule: FocusAidRuleDto
) {
}
/*
{
	"userName": "윤영일",
	"email": "askme143@kaist.ac.kr",
	"timestamp": 123,
	"triggered": false,
	"rule": {
		"ruleId": 1,
		"ruleName": "규칙 이름",
		"activated": true,
		"notiOnTrigger": false,
		"locationTrigger": {
			"latitude": 36.3747234,
			"longitude": 127.3589662,
			"range": 150,
			"locationName": "한국과학기술원"
		},
		"timeTrigger": {
			"startTimeInMinutes": 1239,
			"endTimeInMinutes": 1299,
			"repeatDay": [false,false,false,false,false,true,false]
		},
		"activityTrigger": {
			"activity": "Driving"
		},
		"appBlockAction": {
			"appBlockEntryList": [{"packageName":"com.discord","allowedTimeInMinutes":0,"handlingAction":0}],
			"allAppBlock": false,
			"allAppHandlingAction": 0
		},
		"notificationAction": {
			"appList": ["com.discord"],
			"allApp": false,
			"keywordList": [{"keyword":"냠냠","inclusion":true}],
			"handlingAction":0
		},
		"dndAction": true,
		"ringerAction": 2
	}
}
 */