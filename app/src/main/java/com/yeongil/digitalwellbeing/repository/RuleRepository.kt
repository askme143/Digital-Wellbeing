package com.yeongil.digitalwellbeing.repository

import android.util.Log
import com.yeongil.digitalwellbeing.data.rule.Rule
import com.yeongil.digitalwellbeing.data.rule.RuleInfo
import com.yeongil.digitalwellbeing.data.trigger.LocationTrigger
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dao.rule.RuleDao
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.rule.RuleDto
import com.yeongil.digitalwellbeing.dataSource.SequenceNumber
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.action.AppBlockActionDto
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.action.DndActionDto
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.action.NotificationActionDto
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.action.RingerActionDto
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.rule.RuleInfoDto
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.trigger.ActivityTriggerDto
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.trigger.LocationTriggerDto
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.trigger.TimeTriggerDto
import com.yeongil.digitalwellbeing.utils.TEMPORAL_RULE_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RuleRepository(
    private val sequenceNumber: SequenceNumber,
    private val ruleDao: RuleDao,
) {
    suspend fun insertOrUpdateRule(rule: Rule) {
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

        val ruleDto = RuleDto(
            RuleInfoDto(rid, rule.ruleInfo.copy(ruleId = rid)),
            rule.locationTrigger?.let { LocationTriggerDto(rid, it) },
            rule.timeTrigger?.let { TimeTriggerDto(rid, it) },
            rule.activityTrigger?.let { ActivityTriggerDto(rid, it) },
            rule.appBlockAction?.let { AppBlockActionDto(rid, it) },
            rule.notificationAction?.let { NotificationActionDto(rid, it) },
            rule.dndAction?.let { DndActionDto(rid, it) },
            rule.ringerAction?.let { RingerActionDto(rid, it) },
        )

        if (rule.ruleInfo.ruleId == TEMPORAL_RULE_ID) {
            ruleDao.insertRule(ruleDto)
        } else {
            ruleDao.updateRule(ruleDto)
        }
    }

    suspend fun deleteRuleByRid(rid: Int) {
        ruleDao.deleteRuleByRid(rid)
    }

    suspend fun updateRuleInfo(ruleInfo: RuleInfo) {
        ruleDao.updateRuleInfo(RuleInfoDto(ruleInfo.ruleId, ruleInfo))
    }

    suspend fun getRuleByRid(rid: Int): Rule {
        return ruleDtoToRule(ruleDao.getRuleByRid(rid))
    }

    fun getRuleListAsFlow(): Flow<List<Rule>> {
        return ruleDao.getRuleListAsFlowByRid().map { list ->
            list.map { ruleDtoToRule(it) }
        }
    }

    fun getActiveRuleListAsFlow(): Flow<List<Rule>> {
        return getRuleListAsFlow().map { list -> list.filter { it.ruleInfo.activated } }
    }

    fun getRuleInfoListFlow(): Flow<List<RuleInfo>> {
        return ruleDao.getRuleInfoListFlow().map { it.map { ruleInfoDto -> ruleInfoDto.ruleInfo } }
    }

    fun getLocationTriggerListFlow(): Flow<List<LocationTrigger>> {
        return ruleDao.getLocationTriggerListFlow().map { list -> list.map { it.locationTrigger } }
    }

    private fun ruleDtoToRule(ruleDto: RuleDto): Rule {
        return Rule(
            ruleDto.ruleInfoDto.ruleInfo,
            ruleDto.locationTriggerDto?.locationTrigger,
            ruleDto.timeTriggerDto?.timeTrigger,
            ruleDto.activityTriggerDto?.activityTrigger,
            ruleDto.appBlockActionDto?.appBlockAction,
            ruleDto.notificationActionDto?.notificationAction,
            ruleDto.dndActionDto?.dndAction,
            ruleDto.ringerActionDto?.ringerAction
        )
    }
}