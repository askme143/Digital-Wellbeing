package com.yeongil.focusaid.dataSource.ruleDatabase.dao.trigger

import androidx.room.*
import com.yeongil.focusaid.dataSource.ruleDatabase.dto.trigger.ActivityTriggerDto

@Dao
interface ActivityTriggerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityTrigger(activityTriggerDto: ActivityTriggerDto)

    @Update
    suspend fun updateActivityTrigger(activityTriggerDto: ActivityTriggerDto)

    @Query("DELETE FROM activity_triggers WHERE rid = :rid")
    suspend fun deleteActivityTriggerByRid(rid: Int)
}