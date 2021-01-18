package com.yeongil.digitalwellbeing.data.dao.rule

import androidx.room.*
import com.yeongil.digitalwellbeing.data.dto.rule.RuleInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface RuleInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRuleInfo(ruleInfo: RuleInfo)

    @Update
    suspend fun updateRuleInfo(ruleInfo: RuleInfo)

    @Delete
    suspend fun deleteRuleInfo(ruleInfo: RuleInfo)

    @Query("SELECT * FROM rule_info")
    suspend fun getRuleInfoList(): List<RuleInfo>

    @Query("SELECT * FROM rule_info")
    fun getRuleInfoListFlow(): Flow<List<RuleInfo>>

    @Query("DELETE FROM rule_info WHERE rid = :rid")
    suspend fun deleteRuleInfoByRid(rid: Int)
}