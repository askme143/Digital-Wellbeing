package com.yeongil.focusaid.data.rule

import android.os.Parcelable
import com.yeongil.focusaid.data.rule.action.AppBlockAction
import com.yeongil.focusaid.data.rule.action.DndAction
import com.yeongil.focusaid.data.rule.action.NotificationAction
import com.yeongil.focusaid.data.rule.action.RingerAction
import com.yeongil.focusaid.data.rule.trigger.ActivityTrigger
import com.yeongil.focusaid.data.rule.trigger.LocationTrigger
import com.yeongil.focusaid.data.rule.trigger.TimeTrigger
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Rule(
    val ruleInfo: RuleInfo = RuleInfo(),
    val locationTrigger: LocationTrigger? = null,
    val timeTrigger: TimeTrigger? = null,
    val activityTrigger: ActivityTrigger? = null,

    val appBlockAction: AppBlockAction? = null,
    val notificationAction: NotificationAction? = null,
    val dndAction: DndAction? = null,
    val ringerAction: RingerAction? = null,
) : Parcelable {}