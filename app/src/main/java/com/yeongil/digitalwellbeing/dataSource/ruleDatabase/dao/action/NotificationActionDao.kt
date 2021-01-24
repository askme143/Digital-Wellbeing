package com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dao.action

import androidx.room.*
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.action.NotificationActionDto

@Dao
interface NotificationActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotificationAction(notificationActionDto: NotificationActionDto)

    @Update
    suspend fun updateNotificationAction(notificationActionDto: NotificationActionDto)

    @Query("DELETE FROM notification_actions WHERE rid = :rid")
    suspend fun deleteNotificationActionByRid(rid: Int)
}