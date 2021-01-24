package com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dao.action

import androidx.room.*
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.action.DndActionDto

@Dao
interface DndActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDndAction(dndActionDto: DndActionDto)

    @Update
    suspend fun updateDndAction(dndActionDto: DndActionDto)

    @Query("DELETE FROM dnd_actions WHERE rid = :rid")
    suspend fun deleteDndActionByRid(rid: Int)
}