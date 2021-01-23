package com.yeongil.digitalwellbeing.database.ruleDatabase.dao.action

import androidx.room.*
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.AppBlockActionDto

@Dao
interface AppBlockActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppBlockAction(appBlockActionDto: AppBlockActionDto)

    @Update
    suspend fun updateAppBlockAction(appBlockActionDto: AppBlockActionDto)

    @Delete
    suspend fun deleteAppBlockAction(appBlockActionDto: AppBlockActionDto)

    @Query("DELETE FROM app_block_actions WHERE rid = :rid")
    suspend fun deleteAppBlockActionByRid(rid: Int)
}