package com.yeongil.focusaid.dataSource.ruleDatabase.dao.action

import androidx.room.*
import com.yeongil.focusaid.dataSource.ruleDatabase.dto.action.AppBlockActionDto

@Dao
interface AppBlockActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppBlockAction(appBlockActionDto: AppBlockActionDto)

    @Update
    suspend fun updateAppBlockAction(appBlockActionDto: AppBlockActionDto)

    @Query("DELETE FROM app_block_actions WHERE rid = :rid")
    suspend fun deleteAppBlockActionByRid(rid: Int)
}