package com.yeongil.focusaid.dataSource.logDatabase.dao

import androidx.room.Insert
import androidx.room.Query
import com.yeongil.focusaid.dataSource.logDatabase.dto.RuleDeleteLogDto

interface RuleDeleteLogDao {
    @Insert
    suspend fun insertRuleDeleteLog(ruleDeleteLogDto: RuleDeleteLogDto)

    @Query("DELETE FROM rule_delete_log WHERE id = :id")
    suspend fun deleteRuleDeleteLog(id: Int)

    @Query("SELECT * FROM rule_delete_log")
    suspend fun getAllRuleDeleteLog(): List<RuleDeleteLogDto>
}