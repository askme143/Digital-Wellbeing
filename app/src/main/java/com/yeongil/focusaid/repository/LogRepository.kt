package com.yeongil.focusaid.repository

import android.util.Log
import com.yeongil.focusaid.data.rule.Rule
import com.yeongil.focusaid.dataSource.focusAidApi.FocusAidService
import com.yeongil.focusaid.dataSource.focusAidApi.dto.Confirmed
import com.yeongil.focusaid.dataSource.focusAidApi.dto.RuleTriggerLogDto as FocusAidRuleTriggerLogDto
import com.yeongil.focusaid.dataSource.focusAidApi.dto.RuleConfirmLogDto as FocusAidRuleConfirmLogDto
import com.yeongil.focusaid.dataSource.focusAidApi.dto.RuleActivationLogDto as FocusAidRuleActivationLogDto
import com.yeongil.focusaid.dataSource.focusAidApi.dto.RuleDeleteLogDto as FocusAidRuleDeleteLogDto
import com.yeongil.focusaid.dataSource.focusAidApi.dto.RuleDto as FocusAidRuleDto
import com.yeongil.focusaid.dataSource.focusAidApi.dto.RuleLogDto as FocusAidRuleLogDto
import com.yeongil.focusaid.dataSource.logDatabase.dao.LogDao
import com.yeongil.focusaid.dataSource.logDatabase.dto.*
import com.yeongil.focusaid.dataSource.user.UserInfoDto
import com.yeongil.focusaid.dataSource.user.UserInfoPref
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

class LogRepository(
    private val focusAidService: FocusAidService,
    private val userInfoPref: UserInfoPref,
    private val logDao: LogDao
) {
    private val json = Json { encodeDefaults = false }

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

        logDao.insertRuleLog(RuleLogDto(log = json.encodeToString(ruleLog)))
    }

    suspend fun createRuleDeleteLog(ruleId: Int) {
        val userInfo = userInfoPref.getUserInfo()
        if (userInfo == null) {
            /* TODO: Show user info form */
            return
        }

        val userName = userInfo.userName
        val email = userInfo.email
        val timestamp = Calendar.getInstance().timeInMillis
        val ruleDeleteLog = FocusAidRuleDeleteLogDto(userName, email, timestamp, ruleId)

        try {
            val response = focusAidService.postRuleDeleteLog(ruleDeleteLog)
            if (response.code() == 201) return
        } catch (error: Exception) {
            Log.e("hello", error.message.toString())
        }

        logDao.insertRuleDeleteLog(RuleDeleteLogDto(log = json.encodeToString(ruleDeleteLog)))
    }

    suspend fun createRuleActivationLog(rule: Rule) {
        val userInfo = userInfoPref.getUserInfo()
        if (userInfo == null) {
            /* TODO: Show user info form */
            return
        }

        val userName = userInfo.userName
        val email = userInfo.email
        val timestamp = Calendar.getInstance().timeInMillis
        val activationLog =
            FocusAidRuleActivationLogDto(userName, email, timestamp, FocusAidRuleDto(rule))

        try {
            val response = focusAidService.postRuleActivationLog(activationLog)
            if (response.code() == 201) return
        } catch (error: Exception) {
            Log.e("hello", error.message.toString())
        }

        logDao.insertRuleActivationLog(RuleActivationLogDto(log = json.encodeToString(activationLog)))
    }

    suspend fun createRuleConfirmLog(rule: Rule, confirmed: Confirmed) {
        val userInfo = userInfoPref.getUserInfo()
        if (userInfo == null) {
            /* TODO: Show user info form */
            return
        }

        val userName = userInfo.userName
        val email = userInfo.email
        val timestamp = Calendar.getInstance().timeInMillis
        val confirmLog =
            FocusAidRuleConfirmLogDto(userName, email, timestamp, confirmed.value, FocusAidRuleDto(rule))

        try {
            val response = focusAidService.postRuleConfirmLog(confirmLog)
            if (response.code() == 201) return
        } catch (error: Exception) {
            Log.e("hello", error.message.toString())
        }

        logDao.insertRuleConfirmLog(RuleConfirmLogDto(log = json.encodeToString(confirmLog)))
    }

    suspend fun createRuleTriggerLog(rule: Rule, triggered: Boolean) {
        val userInfo = userInfoPref.getUserInfo()
        if (userInfo == null) {
            /* TODO: Show user info form */
            return
        }

        val userName = userInfo.userName
        val email = userInfo.email
        val timestamp = Calendar.getInstance().timeInMillis
        val triggerLog =
            FocusAidRuleTriggerLogDto(userName, email, timestamp, triggered, FocusAidRuleDto(rule))

        try {
            val response = focusAidService.postRuleTriggerLog(triggerLog)
            if (response.code() == 201) return
        } catch (error: Exception) {
            Log.e("hello", error.message.toString())
        }

        logDao.insertRuleTriggerLog(RuleTriggerLogDto(log = json.encodeToString(triggerLog)))
    }

    companion object {
        const val IGNORE = "IGNORED"
        const val REMOVED = "REMOVED"
        const val CONFIRMED = "CONFIRMED"
    }
}