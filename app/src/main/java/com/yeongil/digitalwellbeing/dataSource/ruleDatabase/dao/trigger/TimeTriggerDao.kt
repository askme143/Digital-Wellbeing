package com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dao.trigger

import androidx.room.*
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.trigger.TimeTriggerDto

@Dao
interface TimeTriggerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeTrigger(timeTriggerDto: TimeTriggerDto)

    @Update
    suspend fun updateTimeTrigger(timeTriggerDto: TimeTriggerDto)

    @Query("DELETE FROM time_triggers WHERE rid = :rid")
    suspend fun deleteTimeTriggerByRid(rid: Int)
}