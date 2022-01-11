package com.yeongil.focusaid.dataSource.ruleDatabase.dao.action

import androidx.room.*
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.NotificationActionEntity

@Dao
interface NotificationActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotificationAction(notificationActionEntity: NotificationActionEntity)

    @Update
    suspend fun updateNotificationAction(notificationActionEntity: NotificationActionEntity)

    @Query("DELETE FROM notification_actions WHERE rid = :rid")
    suspend fun deleteNotificationActionByRid(rid: Int)
}