package com.yeongil.focusaid.repository

import android.database.sqlite.SQLiteConstraintException
import com.yeongil.focusaid.data.rule.Rule
import com.yeongil.focusaid.data.rule.RuleInfo
import com.yeongil.focusaid.dataSource.SequenceNumber
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.RuleDao
import com.yeongil.focusaid.mapper.toData
import com.yeongil.focusaid.mapper.toEntity
import com.yeongil.focusaid.utils.TEMPORAL_RULE_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RuleRepository(
    private val sequenceNumber: SequenceNumber,
    private val ruleDao: RuleDao,
) {
    suspend fun insertOrUpdateRule(rule: Rule): Boolean {
        val rid =
            if (rule.ruleInfo.ruleId != TEMPORAL_RULE_ID)
                rule.ruleInfo.ruleId
            else {
                val temp = sequenceNumber.getAndIncreaseSeqNumber()
                if (temp == TEMPORAL_RULE_ID)
                    sequenceNumber.getAndIncreaseSeqNumber()
                else
                    temp
            }

        val ruleEntity = rule.toEntity(rid)

        return if (rule.ruleInfo.ruleId == TEMPORAL_RULE_ID) {
            try {
                ruleDao.insertRule(ruleEntity)
                true
            } catch (exception: SQLiteConstraintException) {
                false
            }
        } else {
            ruleDao.updateRule(ruleEntity)
            true
        }
    }

    suspend fun deleteRuleByRid(rid: Int) {
        ruleDao.deleteRuleByRid(rid)
    }

    suspend fun updateRuleInfo(ruleInfo: RuleInfo): Boolean {
        return try {
            ruleDao.updateRuleInfo(ruleInfo.toEntity())
            true
        } catch (exception: SQLiteConstraintException) {
            false
        }
    }

    suspend fun getRuleByRid(rid: Int): Rule {
        return ruleDao.getRuleByRid(rid).toData()
    }

    suspend fun getActiveRuleList(): List<Rule> {
        return ruleDao.getRuleList()
            .filter { it.ruleInfoEntity.activated }
            .map { it.toData() }
    }

    fun getRuleInfoListFlow(): Flow<List<RuleInfo>> {
        return ruleDao.getRuleInfoListFlow().map { it.map { ruleInfoEntity -> ruleInfoEntity.toData() } }
    }
}