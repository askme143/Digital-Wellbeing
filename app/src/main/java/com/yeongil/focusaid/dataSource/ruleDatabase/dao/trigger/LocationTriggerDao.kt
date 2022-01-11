package com.yeongil.focusaid.dataSource.ruleDatabase.dao.trigger

import androidx.room.*
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.LocationTriggerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationTriggerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationTrigger(locationTriggerEntity: LocationTriggerEntity)

    @Update
    suspend fun updateLocationTrigger(locationTriggerEntity: LocationTriggerEntity)

    @Query("DELETE FROM location_triggers WHERE rid = :rid")
    suspend fun deleteLocationTriggerByRid(rid: Int)

    @Query("SELECT * FROM location_triggers")
    fun getLocationTriggerListFlow(): Flow<List<LocationTriggerEntity>>
}