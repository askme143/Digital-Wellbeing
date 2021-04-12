package com.yeongil.focusaid.dataSource.logDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yeongil.focusaid.dataSource.logDatabase.dto.RuleConfirmLogDto

@Dao
interface RuleConfirmLogDao {
    @Insert
    suspend fun insertRuleConfirmLog(ruleConfirmLogDto: RuleConfirmLogDto)

    @Query("DELETE FROM rule_confirm_log WHERE id = :id")
    suspend fun deleteRuleConfirmLog(id: Int)

    @Query("SELECT * FROM rule_confirm_log")
    suspend fun getAllRuleConfirmLog(): List<RuleConfirmLogDto>
}