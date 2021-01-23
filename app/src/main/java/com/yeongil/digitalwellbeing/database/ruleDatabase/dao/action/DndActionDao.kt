package com.yeongil.digitalwellbeing.database.ruleDatabase.dao.action

import androidx.room.*
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.DndAction

@Dao
interface DndActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDndAction(dndAction: DndAction)

    @Update
    suspend fun updateDndAction(dndAction: DndAction)

    @Delete
    suspend fun deleteDndAction(dndAction: DndAction)

    @Query("DELETE FROM dnd_actions WHERE rid = :rid")
    suspend fun deleteDndActionByRid(rid: Int)
}