package com.yeongil.digitalwellbeing.data.dao.action

import androidx.room.*
import com.yeongil.digitalwellbeing.data.dto.action.AppBlockAction

@Dao
interface AppBlockActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppBlockAction(appBlockAction: AppBlockAction)

    @Update
    suspend fun updateAppBlockAction(appBlockAction: AppBlockAction)

    @Delete
    suspend fun deleteAppBlockAction(appBlockAction: AppBlockAction)

    @Query("DELETE FROM app_block_actions WHERE rid = :rid")
    suspend fun deleteAppBlockActionByRid(rid: Int)
}