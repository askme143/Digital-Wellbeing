package com.yeongil.focusaid.dataSource.ruleDatabase.dao.trigger

import androidx.room.*
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.TimeTriggerEntity

@Dao
interface TimeTriggerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeTrigger(timeTriggerEntity: TimeTriggerEntity)

    @Update
    suspend fun updateTimeTrigger(timeTriggerEntity: TimeTriggerEntity)

    @Query("DELETE FROM time_triggers WHERE rid = :rid")
    suspend fun deleteTimeTriggerByRid(rid: Int)
}