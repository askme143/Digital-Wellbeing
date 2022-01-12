package com.yeongil.focusaid.dataSource.ruleDatabase.dao.action

import androidx.room.*
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.DndActionEntity

@Dao
interface DndActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDndAction(dndActionEntity: DndActionEntity)

    @Update
    suspend fun updateDndAction(dndActionEntity: DndActionEntity)

    @Query("DELETE FROM dnd_action WHERE rid = :rid")
    suspend fun deleteDndActionByRid(rid: Int)
}