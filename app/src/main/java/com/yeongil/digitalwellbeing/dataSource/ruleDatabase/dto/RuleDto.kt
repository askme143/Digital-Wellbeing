package com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.action.AppBlockActionDto
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.action.DndActionDto
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.action.NotificationActionDto
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.action.RingerActionDto
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.trigger.ActivityTriggerDto
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.trigger.LocationTriggerDto
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.trigger.TimeTriggerDto
import kotlinx.serialization.Serializable

@Serializable
data class RuleDto(
    @Embedded val ruleInfoDto: RuleInfoDto,
    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val locationTriggerDto: LocationTriggerDto?,
    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val timeTriggerDto: TimeTriggerDto?,
    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val activityTriggerDto: ActivityTriggerDto?,

    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val appBlockActionDto: AppBlockActionDto?,
    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val notificationActionDto: NotificationActionDto?,
    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val dndActionDto: DndActionDto?,
    @Relation(
        parentColumn = "rid",
        entityColumn = "rid",
    )
    val ringerActionDto: RingerActionDto?,
)