package com.yeongil.digitalwellbeing.database.ruleDatabase.dao.trigger

import androidx.room.*
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger.LocationTrigger

@Dao
interface LocationTriggerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationTrigger(locationTrigger: LocationTrigger)

    @Update
    suspend fun updateLocationTrigger(locationTrigger: LocationTrigger)

    @Delete
    suspend fun deleteLocationTrigger(locationTrigger: LocationTrigger)

    @Query("DELETE FROM location_triggers WHERE rid = :rid")
    suspend fun deleteLocationTriggerByRid(rid: Int)
}