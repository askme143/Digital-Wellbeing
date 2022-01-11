package com.yeongil.focusaid.dataSource.ruleDatabase.dao

import androidx.room.*
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.RuleInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RuleInfoDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertRuleInfo(ruleInfoEntity: RuleInfoEntity)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateRuleInfo(ruleInfoEntity: RuleInfoEntity)

    @Query("DELETE FROM rule_info WHERE rid = :rid")
    suspend fun deleteRuleInfoByRid(rid: Int)

    @Query("SELECT * FROM rule_info")
    fun getRuleInfoListFlow(): Flow<List<RuleInfoEntity>>
}