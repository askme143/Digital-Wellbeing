package com.yeongil.focusaid.dataSource.ruleDatabase.dao

import androidx.room.*
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.RuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RuleDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertRule(ruleInfoEntity: RuleEntity)

    @Update
    suspend fun updateRule(ruleInfoEntity: RuleEntity)

    @Query("DELETE FROM rule WHERE rid = :rid")
    suspend fun deleteRuleByRid(rid: Int)

    @Query("SELECT * FROM rule")
    fun getRuleListFlow(): Flow<List<RuleEntity>>
}