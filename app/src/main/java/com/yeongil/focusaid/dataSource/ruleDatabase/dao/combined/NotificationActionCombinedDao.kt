package com.yeongil.focusaid.dataSource.ruleDatabase.dao.combined

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.action.NotificationActionDao
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.combined.NotificationActionCombined

@Dao
interface NotificationActionCombinedDao : NotificationActionDao {
    @Transaction
    suspend fun insertNotificationActionCombined(notificationActionCombined: NotificationActionCombined) {
        with(notificationActionCombined) {
            insertNotificationAction(notificationActionEntity)
            insertKeywordEntryList(keywordEntryEntityList)
            insertPackageNameList(packageNameEntityList)
        }
    }

    @Transaction
    suspend fun updateNotificationActionCombined(notificationActionCombined: NotificationActionCombined) {
        with(notificationActionCombined) {
            val rid = notificationActionEntity.rid

            updateNotificationAction(notificationActionEntity)

            deleteKeywordEntryListByRid(rid)
            insertKeywordEntryList(keywordEntryEntityList)

            deletePackageNameListByRid(rid)
            insertPackageNameList(packageNameEntityList)
        }
    }

    @Query("DELETE FROM app_block_action WHERE rid = :rid")
    suspend fun deleteNotificationActionCombinedByRid(rid: Int) {
        deleteNotificationActionByRid(rid)
        deleteKeywordEntryListByRid(rid)
        deletePackageNameListByRid(rid)
    }
}