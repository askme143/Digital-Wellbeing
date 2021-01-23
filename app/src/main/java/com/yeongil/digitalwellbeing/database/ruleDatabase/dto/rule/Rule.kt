package com.yeongil.digitalwellbeing.database.ruleDatabase.dto.rule

import androidx.room.Embedded
import androidx.room.Relation
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.AppBlockAction
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.DndAction
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.NotificationAction
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.RingerAction
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger.ActivityTrigger
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger.LocationTrigger
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger.TimeTrigger
import kotlinx.serialization.Serializable

@Serializable
data class Rule(
    @Embedded val ruleInfo: RuleInfo,
    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val locationTrigger: LocationTrigger?,
    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val timeTrigger: TimeTrigger?,
    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val activityTrigger: ActivityTrigger?,

    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val appBlockAction: AppBlockAction?,
    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val notificationAction: NotificationAction?,
    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val dndAction: DndAction?,
    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val ringerAction: RingerAction?,
)