package com.yeongil.focusaid.dataSource.ruleDatabase.dao.action

import androidx.room.*
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.RingerActionEntity

@Dao
interface RingerActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRingerAction(ringerActionEntity: RingerActionEntity)

    @Update
    suspend fun updateRingerAction(ringerActionEntity: RingerActionEntity)

    @Query("DELETE FROM ringer_actions WHERE rid = :rid")
    suspend fun deleteRingerActionByRid(rid: Int)
}