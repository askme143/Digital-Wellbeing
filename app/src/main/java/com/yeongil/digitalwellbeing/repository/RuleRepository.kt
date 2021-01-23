package com.yeongil.digitalwellbeing.repository

import com.yeongil.digitalwellbeing.database.ruleDatabase.dao.rule.RuleDao
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.rule.RuleDto
import com.yeongil.digitalwellbeing.utils.TEMPORAL_RID
import com.yeongil.digitalwellbeing.utils.SequenceNumber
import java.lang.IllegalArgumentException

class RuleRepository(
    private val sequenceNumber: SequenceNumber,
    private val ruleDao: RuleDao,
) {
    suspend fun insertNewRule(ruleDto: RuleDto) {
        var rid = sequenceNumber.getAndIncreaseSeqNumber()
        if (rid == TEMPORAL_RID) rid = sequenceNumber.getAndIncreaseSeqNumber()

        val saveRule = RuleDto(
            ruleDto.ruleInfoDto.copy(rid = rid),
            ruleDto.locationTriggerDto?.copy(rid = rid),
            ruleDto.timeTriggerDto?.copy(rid = rid),
            ruleDto.activityTriggerDto?.copy(rid = rid),
            ruleDto.appBlockActionDto?.copy(rid = rid),
            ruleDto.notificationActionDto?.copy(rid = rid),
            ruleDto.dndActionDto?.copy(rid = rid),
            ruleDto.ringerActionDto?.copy(rid = rid)
        )

        ruleDao.insertRule((saveRule))
    }

    suspend fun updateRule(ruleDto: RuleDto) {
        if (ruleDto.ruleInfoDto.rid == TEMPORAL_RID) throw(IllegalArgumentException("RID should not be 0"))
        ruleDao.updateRule(ruleDto)
    }
}