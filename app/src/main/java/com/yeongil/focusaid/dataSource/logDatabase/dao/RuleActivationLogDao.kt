package com.yeongil.focusaid.dataSource.logDatabase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yeongil.focusaid.dataSource.logDatabase.dto.RuleActivationLogDto

@Dao
interface RuleActivationLogDao {
    @Insert
    suspend fun insertRuleActivationLog(ruleActivationLogDto: RuleActivationLogDto)

    @Query("DELETE FROM rule_activation_log WHERE id = :id")
    suspend fun deleteRuleActivationLog(id: Int)

    @Query("SELECT * FROM rule_activation_log")
    suspend fun getAllRuleActivationLog(): List<RuleActivationLogDto>
}