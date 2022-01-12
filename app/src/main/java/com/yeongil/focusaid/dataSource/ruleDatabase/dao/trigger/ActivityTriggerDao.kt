package com.yeongil.focusaid.dataSource.ruleDatabase.dao.trigger

import androidx.room.*
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.ActivityTriggerEntity

@Dao
interface ActivityTriggerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityTrigger(activityTriggerEntity: ActivityTriggerEntity)

    @Update
    suspend fun updateActivityTrigger(activityTriggerEntity: ActivityTriggerEntity)

    @Query("DELETE FROM activity_trigger WHERE rid = :rid")
    suspend fun deleteActivityTriggerByRid(rid: Int)
}