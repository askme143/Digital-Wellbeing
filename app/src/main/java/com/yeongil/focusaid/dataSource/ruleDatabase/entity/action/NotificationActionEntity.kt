package com.yeongil.focusaid.dataSource.ruleDatabase.entity.action

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeongil.focusaid.data.rule.action.NotificationAction
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "notification_actions")
data class NotificationActionEntity(
    @PrimaryKey val rid: Int,
    @Embedded val notificationAction: NotificationAction
)