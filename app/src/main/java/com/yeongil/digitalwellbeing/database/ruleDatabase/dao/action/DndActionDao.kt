package com.yeongil.digitalwellbeing.database.ruleDatabase.dao.action

import androidx.room.*
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.DndActionDto

@Dao
interface DndActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDndAction(dndActionDto: DndActionDto)

    @Update
    suspend fun updateDndAction(dndActionDto: DndActionDto)

    @Delete
    suspend fun deleteDndAction(dndActionDto: DndActionDto)

    @Query("DELETE FROM dnd_actions WHERE rid = :rid")
    suspend fun deleteDndActionByRid(rid: Int)
}