package com.yeongil.digitalwellbeing.database.ruleDatabase.dao.trigger

import androidx.room.*
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger.ActivityTriggerDto

@Dao
interface ActivityTriggerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityTrigger(activityTriggerDto: ActivityTriggerDto)

    @Update
    suspend fun updateActivityTrigger(activityTriggerDto: ActivityTriggerDto)

    @Delete
    suspend fun deleteActivityTrigger(activityTriggerDto: ActivityTriggerDto)

    @Query("DELETE FROM activity_triggers WHERE rid = :rid")
    suspend fun deleteActivityTriggerByRid(rid: Int)
}