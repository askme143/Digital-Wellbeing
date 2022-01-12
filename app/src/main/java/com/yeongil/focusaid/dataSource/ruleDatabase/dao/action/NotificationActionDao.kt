package com.yeongil.focusaid.dataSource.ruleDatabase.dao.action

import androidx.room.*
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.NotificationActionEntity

@Dao
interface NotificationActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotificationAction(notificationActionEntity: NotificationActionEntity)

    @Update
    suspend fun updateNotificationAction(notificationActionEntity: NotificationActionEntity)

    @Query("DELETE FROM notification_action WHERE rid = :rid")
    suspend fun deleteNotificationActionByRid(rid: Int)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKeywordEntryList(keywordEntryEntityList: List<NotificationActionEntity.KeywordEntryEntity>)

    @Update
    suspend fun updateKeywordEntryList(keywordEntryEntityList: List<NotificationActionEntity.KeywordEntryEntity>)

    @Query("Delete FROM keyword_entry WHERE rid = :rid")
    suspend fun deleteKeywordEntryListByRid(rid: Int)

    @Query("Delete FROM keyword_entry WHERE rid = :rid and id not in (:idList)")
    suspend fun deleteKeywordEntryListByRidExceptId(rid: Int, idList: List<Int>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPackageNameList(packageNameEntityList: List<NotificationActionEntity.PackageNameEntity>)

    @Update
    suspend fun updatePackageNameList(packageNameEntityList: List<NotificationActionEntity.PackageNameEntity>)

    @Query("Delete FROM package_name WHERE rid = :rid")
    suspend fun deletePackageNameListByRid(rid: Int)

    @Query("Delete FROM package_name WHERE rid = :rid and id not in (:idList)")
    suspend fun deletePackageNameListByRidExceptId(rid: Int, idList: List<Int>)
}