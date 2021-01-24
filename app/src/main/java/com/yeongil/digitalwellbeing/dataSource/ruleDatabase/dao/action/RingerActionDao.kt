package com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dao.action

import androidx.room.*
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.action.RingerActionDto

@Dao
interface RingerActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRingerAction(ringerActionDto: RingerActionDto)

    @Update
    suspend fun updateRingerAction(ringerActionDto: RingerActionDto)

    @Query("DELETE FROM ringer_actions WHERE rid = :rid")
    suspend fun deleteRingerActionByRid(rid: Int)
}