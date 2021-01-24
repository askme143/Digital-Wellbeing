package com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.action

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeongil.digitalwellbeing.data.action.NotificationAction
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "notification_actions")
data class NotificationActionDto(
    @PrimaryKey val rid: Int,
    @Embedded val notificationAction: NotificationAction
)