package com.yeongil.focusaid.repository

import com.yeongil.focusaid.data.rule.Rule
import com.yeongil.focusaid.dataSource.focusAidApi.FocusAidService
import com.yeongil.focusaid.dataSource.focusAidApi.dto.FocusAidRuleDto
import com.yeongil.focusaid.dataSource.focusAidApi.dto.RuleLogDto
import com.yeongil.focusaid.dataSource.user.UserInfoPref
import java.util.*

class LogRepository(
    private val focusAidService: FocusAidService,
    private val userInfoPref: UserInfoPref
) {
    suspend fun createUserLog(rule: Rule, timeTakenInSeconds: Int) {
        val userInfo = userInfoPref.getUserInfo()
        if (userInfo == null) {
            /* TODO: Show user info form */
            return
        }

        val userName = userInfo.userName
        val email = userInfo.email
        val timestamp = Calendar.getInstance().timeInMillis

        try {
            val response = focusAidService.postRuleLog(
                RuleLogDto(
                    userName,
                    email,
                    timestamp,
                    timeTakenInSeconds,
                    FocusAidRuleDto(rule)
                )
            )

            if (response.code() == 201) return
        } catch (error: Exception) {
        }

        /* TODO: Save on local database */
    }
}