package com.yeongil.focusaid.dataSource.logDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yeongil.focusaid.dataSource.logDatabase.dto.RuleLogDto

@Dao
interface RuleLogDao {
    @Insert
    suspend fun insertRuleLog(ruleLogDto: RuleLogDto)

    @Query("DELETE FROM rule_log WHERE id = :id")
    suspend fun deleteRuleLog(id: Int)

    @Query("SELECT * FROM rule_log")
    suspend fun getAllRuleLog(): List<RuleLogDto>
}