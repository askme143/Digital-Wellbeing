package com.yeongil.digitalwellbeing.data.dao.action

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.yeongil.digitalwellbeing.data.dto.action.Action
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface ActionDao {
    @Transaction
    @Query("SELECT * FROM rule_info WHERE rid = :rid")
    fun getActionFlow(rid: Int): Flow<Action>

    fun getActionFlowDistinct(rid: Int) = getActionFlow(rid).distinctUntilChanged()
}