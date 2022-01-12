package com.yeongil.focusaid.dataSource.ruleDatabase.dao.action

import androidx.room.*
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.AppBlockActionEntity

@Dao
interface AppBlockActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppBlockAction(appBlockActionEntity: AppBlockActionEntity)

    @Update
    suspend fun updateAppBlockAction(appBlockActionEntity: AppBlockActionEntity)

    @Query("DELETE FROM app_block_action WHERE rid = :rid")
    suspend fun deleteAppBlockActionByRid(rid: Int)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppBlockEntryList(appBlockEntryEntityList: List<AppBlockActionEntity.AppBlockEntryEntity>)

    @Update
    suspend fun updateAppBlockEntryList(appBlockEntryEntityList: List<AppBlockActionEntity.AppBlockEntryEntity>)

    @Query("DELETE FROM app_block_entry WHERE rid = :rid")
    suspend fun deleteAppBlockEntryListByRid(rid: Int)

    @Query("DELETE FROM app_block_entry WHERE rid = :rid and id not in (:idList)")
    suspend fun deleteAppBlockEntryListByRidExceptId(rid: Int, idList: List<Int>)
}