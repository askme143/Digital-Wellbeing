package com.yeongil.digitalwellbeing.database.ruleDatabase.dao.trigger

import androidx.room.*
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger.TimeTrigger

@Dao
interface TimeTriggerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeTrigger(timeTrigger: TimeTrigger)

    @Update
    suspend fun updateTimeTrigger(timeTrigger: TimeTrigger)

    @Delete
    suspend fun deleteTimeTrigger(timeTrigger: TimeTrigger)

    @Query("DELETE FROM time_triggers WHERE rid = :rid")
    suspend fun deleteTimeTriggerByRid(rid: Int)
}