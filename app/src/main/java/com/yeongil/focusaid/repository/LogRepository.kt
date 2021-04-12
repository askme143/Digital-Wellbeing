package com.yeongil.focusaid.repository

import android.util.Log
import com.yeongil.focusaid.data.rule.Rule
import com.yeongil.focusaid.dataSource.focusAidApi.FocusAidService
import com.yeongil.focusaid.dataSource.focusAidApi.dto.RuleDto as FocusAidRuleDto
import com.yeongil.focusaid.dataSource.focusAidApi.dto.RuleLogDto as FocusAidRuleLogDto
import com.yeongil.focusaid.dataSource.logDatabase.dto.RuleLogDto
import com.yeongil.focusaid.dataSource.logDatabase.dao.LogDao
import com.yeongil.focusaid.dataSource.user.UserInfoPref
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class LogRepository(
    private val focusAidService: FocusAidService,
    private val userInfoPref: UserInfoPref,
    private val logDao: LogDao
) {
    suspend fun createRuleLog(rule: Rule, timeTakenInSeconds: Int) {
        val userInfo = userInfoPref.getUserInfo()
        if (userInfo == null) {
            /* TODO: Show user info form */
            return
        }

        val userName = userInfo.userName
        val email = userInfo.email
        val timestamp = Calendar.getInstance().timeInMillis
        val ruleLog = FocusAidRuleLogDto(
            userName,
            email,
            timestamp,
            timeTakenInSeconds,
            FocusAidRuleDto(rule)
        )

        try {
            val response = focusAidService.postRuleLog(ruleLog)
            if (response.code() == 201) return
        } catch (error: Exception) {
            Log.e("hello", error.message.toString())
        }

        logDao.insertRuleLog(RuleLogDto(log = Json {
            encodeDefaults = false
        }.encodeToString(ruleLog)))
    }
}