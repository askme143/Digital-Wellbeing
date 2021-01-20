package com.yeongil.digitalwellbeing.data.dao.trigger

import androidx.room.*
import com.yeongil.digitalwellbeing.data.dto.trigger.TimeTrigger
import kotlinx.coroutines.flow.Flow

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