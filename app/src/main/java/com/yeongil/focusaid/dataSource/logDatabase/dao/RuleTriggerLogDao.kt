package com.yeongil.focusaid.dataSource.logDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yeongil.focusaid.dataSource.logDatabase.dto.RuleTriggerLogDto

@Dao
interface RuleTriggerLogDao {
    @Insert
    suspend fun insertRuleTriggerLog(ruleTriggerLogDto: RuleTriggerLogDto)

    @Query("DELETE FROM rule_trigger_log WHERE id = :id")
    suspend fun deleteRuleTriggerLog(id: Int)

    @Query("SELECT * FROM rule_trigger_log")
    suspend fun getAllRuleTriggerLog(): List<RuleTriggerLogDto>
}