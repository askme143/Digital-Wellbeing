package com.yeongil.digitalwellbeing.data.rule

import com.yeongil.digitalwellbeing.data.rule.action.AppBlockAction
import com.yeongil.digitalwellbeing.data.rule.action.DndAction
import com.yeongil.digitalwellbeing.data.rule.action.NotificationAction
import com.yeongil.digitalwellbeing.data.rule.action.RingerAction
import com.yeongil.digitalwellbeing.data.rule.trigger.ActivityTrigger
import com.yeongil.digitalwellbeing.data.rule.trigger.LocationTrigger
import com.yeongil.digitalwellbeing.data.rule.trigger.TimeTrigger
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.RuleDto
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
) {
    constructor(ruleDto: RuleDto) : this(
        ruleDto.ruleInfoDto.ruleInfo,
        ruleDto.locationTriggerDto?.locationTrigger,
        ruleDto.timeTriggerDto?.timeTrigger,
        ruleDto.activityTriggerDto?.activityTrigger,
        ruleDto.appBlockActionDto?.appBlockAction,
        ruleDto.notificationActionDto?.notificationAction,
        ruleDto.dndActionDto?.dndAction,
        ruleDto.ringerActionDto?.ringerAction,
    )
}