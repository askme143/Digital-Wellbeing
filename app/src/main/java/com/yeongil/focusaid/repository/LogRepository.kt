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
import kotlinx.serialization.decodeFromString
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

        suspend fun checkRemainingLogs() {
        val ruleLogs = logDao.getAllRuleLog()
        val activationLogs = logDao.getAllRuleActivationLog()
        val confirmLogs = logDao.getAllRuleConfirmLog()
        val deleteLogs = logDao.getAllRuleDeleteLog()
        val triggerLogs = logDao.getAllRuleTriggerLog()

        ruleLogs.forEach {
            try {
                val response = focusAidService.postRuleLog(json.decodeFromString(it.log))
                if (response.code() == 201) {
                    logDao.deleteRuleLog(it.id)
                }
                Log.e("hello", response.code().toString())
            } catch (error: Exception) {
                Log.e("hello", error.message.toString())
                return
            }
        }
        activationLogs.forEach {
            try {
                val response = focusAidService.postRuleActivationLog(json.decodeFromString(it.log))
                if (response.code() == 201) {
                    logDao.deleteRuleActivationLog(it.id)
                }
            } catch (error: Exception) {
                Log.e("hello", error.message.toString())
            }
        }
        confirmLogs.forEach {
            try {
                val response = focusAidService.postRuleConfirmLog(json.decodeFromString(it.log))
                if (response.code() == 201) {
                    logDao.deleteRuleConfirmLog(it.id)
                }
            } catch (error: Exception) {
                Log.e("hello", error.message.toString())
            }
        }
        deleteLogs.forEach {
            try {
                val response = focusAidService.postRuleDeleteLog(json.decodeFromString(it.log))
                if (response.code() == 201) {
                    logDao.deleteRuleDeleteLog(it.id)
                }
            } catch (error: Exception) {
                Log.e("hello", error.message.toString())
            }
        }
        triggerLogs.forEach {
            try {
                val response = focusAidService.postRuleTriggerLog(json.decodeFromString(it.log))
                if (response.code() == 201) {
                    logDao.deleteRuleTriggerLog(it.id)
                }
            } catch (error: Exception) {
                Log.e("hello", error.message.toString())
            }
        }
    }
}