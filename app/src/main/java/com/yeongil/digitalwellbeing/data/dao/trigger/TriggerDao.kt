package com.yeongil.digitalwellbeing.data.dao.trigger

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.yeongil.digitalwellbeing.data.dto.trigger.Trigger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface TriggerDao {
    @Transaction
    @Query("SELECT * FROM rule_info WHERE rid = :rid")
    fun getTriggerFlow(rid: Int): Flow<Trigger>

    fun getTriggerFlowDistinct(rid: Int) = getTriggerFlow(rid).distinctUntilChanged()
}