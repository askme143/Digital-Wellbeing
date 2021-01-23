package com.yeongil.digitalwellbeing.database.ruleDatabase.dao.trigger

import androidx.room.*
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger.ActivityTrigger

@Dao
interface ActivityTriggerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityTrigger(activityTrigger: ActivityTrigger)

    @Update
    suspend fun updateActivityTrigger(activityTrigger: ActivityTrigger)

    @Delete
    suspend fun deleteActivityTrigger(activityTrigger: ActivityTrigger)

    @Query("DELETE FROM activity_triggers WHERE rid = :rid")
    suspend fun deleteActivityTriggerByRid(rid: Int)
}