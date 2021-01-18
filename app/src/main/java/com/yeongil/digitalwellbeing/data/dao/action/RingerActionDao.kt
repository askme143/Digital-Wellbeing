package com.yeongil.digitalwellbeing.data.dao.action

import androidx.room.*
import com.yeongil.digitalwellbeing.data.dto.action.RingerAction

@Dao
interface RingerActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRingerAction(ringerAction: RingerAction)

    @Update
    suspend fun updateRingerAction(ringerAction: RingerAction)

    @Delete
    suspend fun deleteRingerAction(ringerAction: RingerAction)

    @Query("DELETE FROM ringer_actions WHERE rid = :rid")
    suspend fun deleteRingerActionByRid(rid: Int)
}