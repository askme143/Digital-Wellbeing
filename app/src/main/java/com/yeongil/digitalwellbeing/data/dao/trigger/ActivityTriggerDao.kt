package com.yeongil.digitalwellbeing.data.dao.trigger

import androidx.room.*
import com.yeongil.digitalwellbeing.data.dto.trigger.ActivityTrigger
import kotlinx.coroutines.flow.Flow

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