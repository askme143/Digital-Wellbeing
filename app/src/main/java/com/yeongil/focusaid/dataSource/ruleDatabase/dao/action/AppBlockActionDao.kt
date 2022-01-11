package com.yeongil.focusaid.dataSource.ruleDatabase.dao.action

import androidx.room.*
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.AppBlockActionEntity

@Dao
interface AppBlockActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppBlockAction(appBlockActionEntity: AppBlockActionEntity)

    @Update
    suspend fun updateAppBlockAction(appBlockActionEntity: AppBlockActionEntity)

    @Query("DELETE FROM app_block_actions WHERE rid = :rid")
    suspend fun deleteAppBlockActionByRid(rid: Int)
}