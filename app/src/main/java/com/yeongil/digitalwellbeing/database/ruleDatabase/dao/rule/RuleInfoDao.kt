package com.yeongil.digitalwellbeing.database.ruleDatabase.dao.rule

import androidx.room.*
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.rule.RuleInfoDto
import kotlinx.coroutines.flow.Flow

@Dao
interface RuleInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRuleInfo(ruleInfoDto: RuleInfoDto)

    @Update
    suspend fun updateRuleInfo(ruleInfoDto: RuleInfoDto)

    @Delete
    suspend fun deleteRuleInfo(ruleInfoDto: RuleInfoDto)

    @Query("SELECT * FROM rule_info")
    suspend fun getRuleInfoList(): List<RuleInfoDto>

    @Query("SELECT * FROM rule_info")
    fun getRuleInfoListFlow(): Flow<List<RuleInfoDto>>

    @Query("DELETE FROM rule_info WHERE rid = :rid")
    suspend fun deleteRuleInfoByRid(rid: Int)
}