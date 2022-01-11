package com.yeongil.focusaid.repository

import android.database.sqlite.SQLiteConstraintException
import com.yeongil.focusaid.data.rule.Rule
import com.yeongil.focusaid.data.rule.RuleInfo
import com.yeongil.focusaid.dataSource.SequenceNumber
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.RuleDao
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.RuleEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.RuleInfoEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.AppBlockActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.DndActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.NotificationActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.RingerActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.ActivityTriggerEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.LocationTriggerEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.TimeTriggerEntity
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

        val ruleDto = RuleEntity(
            RuleInfoEntity(rid, rule.ruleInfo.copy(ruleId = rid)),
            rule.locationTrigger?.let { LocationTriggerEntity(rid, it) },
            rule.timeTrigger?.let { TimeTriggerEntity(rid, it) },
            rule.activityTrigger?.let { ActivityTriggerEntity(rid, it) },
            rule.appBlockAction?.let { AppBlockActionEntity(rid, it) },
            rule.notificationAction?.let { NotificationActionEntity(rid, it) },
            rule.dndAction?.let { DndActionEntity(rid, it) },
            rule.ringerAction?.let { RingerActionEntity(rid, it) },
        )

        return if (rule.ruleInfo.ruleId == TEMPORAL_RULE_ID) {
            try {
                ruleDao.insertRule(ruleDto)
                true
            } catch (exception: SQLiteConstraintException) {
                false
            }
        } else {
            ruleDao.updateRule(ruleDto)
            true
        }
    }

    suspend fun deleteRuleByRid(rid: Int) {
        ruleDao.deleteRuleByRid(rid)
    }

    suspend fun updateRuleInfo(ruleInfo: RuleInfo): Boolean {
        return try {
            ruleDao.updateRuleInfo(RuleInfoEntity(ruleInfo.ruleId, ruleInfo))
            true
        } catch (exception: SQLiteConstraintException) {
            false
        }
    }

    suspend fun getRuleByRid(rid: Int): Rule {
        return Rule(ruleDao.getRuleByRid(rid))
    }

    suspend fun getActiveRuleList(): List<Rule> {
        return ruleDao.getRuleList()
            .filter { it.ruleInfoEntity.ruleInfo.activated }
            .map { Rule(it) }
    }

    fun getRuleInfoListFlow(): Flow<List<RuleInfo>> {
        return ruleDao.getRuleInfoListFlow().map { it.map { ruleInfoDto -> ruleInfoDto.ruleInfo } }
    }
}