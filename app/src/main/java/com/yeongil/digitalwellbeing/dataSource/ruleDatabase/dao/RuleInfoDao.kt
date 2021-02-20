package com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dao

import androidx.room.*
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.RuleInfoDto
import kotlinx.coroutines.flow.Flow

@Dao
interface RuleInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRuleInfo(ruleInfoDto: RuleInfoDto)

    @Update
    suspend fun updateRuleInfo(ruleInfoDto: RuleInfoDto)

    @Query("DELETE FROM rule_info WHERE rid = :rid")
    suspend fun deleteRuleInfoByRid(rid: Int)

    @Query("SELECT * FROM rule_info")
    fun getRuleInfoListFlow(): Flow<List<RuleInfoDto>>
}