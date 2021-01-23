package com.yeongil.digitalwellbeing.database.ruleDatabase.dao.trigger

import androidx.room.*
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger.LocationTriggerDto

@Dao
interface LocationTriggerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationTrigger(locationTriggerDto: LocationTriggerDto)

    @Update
    suspend fun updateLocationTrigger(locationTriggerDto: LocationTriggerDto)

    @Delete
    suspend fun deleteLocationTrigger(locationTriggerDto: LocationTriggerDto)

    @Query("DELETE FROM location_triggers WHERE rid = :rid")
    suspend fun deleteLocationTriggerByRid(rid: Int)
}