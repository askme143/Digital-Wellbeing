package com.yeongil.digitalwellbeing.database.ruleDatabase.dao.action

import androidx.room.*
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.NotificationAction

@Dao
interface NotificationActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotificationAction(notificationAction: NotificationAction)

    @Update
    suspend fun updateNotificationAction(notificationAction: NotificationAction)

    @Delete
    suspend fun deleteNotificationAction(notificationAction: NotificationAction)

    @Query("DELETE FROM notification_actions WHERE rid = :rid")
    suspend fun deleteNotificationActionByRid(rid: Int)
}