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
    val ruleInfo: RuleInfo,
    val locationTrigger: LocationTrigger?,
    val timeTrigger: TimeTrigger?,
    val activityTrigger: ActivityTrigger?,

    val appBlockAction: AppBlockAction?,
    val notificationAction: NotificationAction?,
    val dndAction: DndAction?,
    val ringerAction: RingerAction?,
)