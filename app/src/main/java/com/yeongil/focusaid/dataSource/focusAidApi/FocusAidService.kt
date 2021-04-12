package com.yeongil.focusaid.dataSource.focusAidApi

import com.yeongil.focusaid.dataSource.focusAidApi.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface FocusAidService {
    @POST("/rules")
    suspend fun postRuleLog(@Body ruleLog: RuleLogDto): Response<Unit?>

    @DELETE("/rules")
    suspend fun postRuleDeleteLog(@Body ruleDeleteLog: RuleDeleteLogDto): Response<Unit?>

    @POST("/rules/activation")
    suspend fun postRuleActivationLog(@Body ruleActivationLog: RuleActivationLogDto): Response<Unit?>

    @POST("rules/trigger")
    suspend fun postRuleTriggerLog(@Body ruleTriggerLog: RuleTriggerLogDto): Response<Unit?>

    @POST("rules/confirm")
    suspend fun postRuleConfirmLog(@Body ruleConfirmLog: RuleConfirmLogDto): Response<Unit?>
}