package com.yeongil.focusaid.dataSource.focusAidApi.dto

import com.yeongil.focusaid.data.rule.Rule
import com.yeongil.focusaid.data.rule.action.AppBlockAction
import com.yeongil.focusaid.data.rule.action.NotificationAction
import com.yeongil.focusaid.data.rule.trigger.ActivityTrigger
import com.yeongil.focusaid.data.rule.trigger.LocationTrigger
import com.yeongil.focusaid.data.rule.trigger.TimeTrigger
import kotlinx.serialization.Serializable

@Serializable
data class RuleDto(
    val ruleId: Int,
    val ruleName: String,
    val activated: Boolean,
    val notiOnTrigger: Boolean,
    val locationTrigger: LocationTrigger? = null,
    val timeTrigger: TimeTrigger? = null,
    val activityTrigger: ActivityTrigger? = null,
    val appBlockAction: AppBlockAction? = null,
    val notificationAction: NotificationAction? = null,
    val dndAction: Boolean? = null,
    val ringerAction: Int? = null,
) {
    constructor(rule: Rule): this(
        rule.ruleInfo.ruleId,
        rule.ruleInfo.ruleName,
        rule.ruleInfo.activated,
        rule.ruleInfo.notiOnTrigger,
        rule.locationTrigger,
        rule.timeTrigger,
        rule.activityTrigger,
        rule.appBlockAction,
        rule.notificationAction,
        if (rule.dndAction != null) true else null,
        rule.ringerAction?.ringerMode?.ordinal
    )
}