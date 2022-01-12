package com.yeongil.focusaid.repository

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.yeongil.focusaid.data.rule.Rule
import com.yeongil.focusaid.data.rule.RuleInfo
import com.yeongil.focusaid.dataSource.SequenceNumber
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.combined.RuleCombinedDao
import com.yeongil.focusaid.mapper.toCombined
import com.yeongil.focusaid.mapper.toData
import com.yeongil.focusaid.mapper.toEntity
import com.yeongil.focusaid.utils.TEMPORAL_RULE_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RuleRepository(
    private val sequenceNumber: SequenceNumber,
    private val ruleCombinedDao: RuleCombinedDao,
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

        val ruleCombined = rule.toCombined(rid)

        return if (rule.ruleInfo.ruleId == TEMPORAL_RULE_ID) {
            try {
                ruleCombinedDao.insertRuleCombined(ruleCombined)
                true
            } catch (exception: SQLiteConstraintException) {
                Log.e("helll1", exception.toString())
                Log.e("helll2", rid.toString())
                Log.e(
                    "helll3",
                    ruleCombined.notificationActionCombined?.notificationActionEntity?.rid.toString()
                )
                Log.e(
                    "helll4",
                    ruleCombined.notificationActionCombined?.keywordEntryEntityList?.joinToString { it.rid.toString() }
                        ?: "1")
                Log.e(
                    "helll5",
                    ruleCombined.notificationActionCombined?.packageNameEntityList?.joinToString { it.rid.toString() }
                        ?: "2")
                Log.e(
                    "helll6",
                    ruleCombined.ruleEntity.ruleId.toString()
                )
                false
            }
        } else {
            ruleCombinedDao.updateRuleCombined(ruleCombined)
            true
        }
    }

    suspend fun deleteRuleByRid(rid: Int) {
        ruleCombinedDao.deleteRuleByRid(rid)
    }

    suspend fun updateRuleInfo(ruleInfo: RuleInfo): Boolean {
        return try {
            ruleCombinedDao.updateRule(ruleInfo.toEntity(ruleInfo.ruleId))
            true
        } catch (exception: SQLiteConstraintException) {
            false
        }
    }

    suspend fun getRuleByRid(rid: Int): Rule {
        return ruleCombinedDao.getRuleCombinedByRid(rid).toData()
    }

    suspend fun getActiveRuleList(): List<Rule> {
        return ruleCombinedDao.getRuleCombinedList()
            .filter { it.ruleEntity.activated }
            .map { it.toData() }
    }

    fun getRuleInfoListFlow(): Flow<List<RuleInfo>> {
        return ruleCombinedDao.getRuleListFlow()
            .map { it.map { ruleEntity -> ruleEntity.toData() } }
    }
}