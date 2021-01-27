package com.yeongil.digitalwellbeing.data.rule

import com.yeongil.digitalwellbeing.data.action.AppBlockAction
import com.yeongil.digitalwellbeing.data.action.DndAction
import com.yeongil.digitalwellbeing.data.action.NotificationAction
import com.yeongil.digitalwellbeing.data.action.RingerAction
import com.yeongil.digitalwellbeing.data.trigger.ActivityTrigger
import com.yeongil.digitalwellbeing.data.trigger.LocationTrigger
import com.yeongil.digitalwellbeing.data.trigger.TimeTrigger
import kotlinx.serialization.Serializable

@Serializable
data class Rule(
    val ruleInfo: RuleInfo = RuleInfo(),
    val locationTrigger: LocationTrigger? = null,
    val timeTrigger: TimeTrigger? = null,
    val activityTrigger: ActivityTrigger? = null,

    val appBlockAction: AppBlockAction? = null,
    val notificationAction: NotificationAction? = null,
    val dndAction: DndAction? = null,
    val ringerAction: RingerAction? = null,
)