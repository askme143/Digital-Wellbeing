package com.yeongil.digitalwellbeing.repository

import com.yeongil.digitalwellbeing.data.dao.rule.RuleDao
import com.yeongil.digitalwellbeing.data.dto.rule.Rule
import com.yeongil.digitalwellbeing.utils.TEMPORAL_RID
import com.yeongil.digitalwellbeing.utils.SequenceNumber
import java.lang.IllegalArgumentException

class RuleRepository(
    private val sequenceNumber: SequenceNumber,
    private val ruleDao: RuleDao,
) {
    suspend fun insertNewRule(rule: Rule) {
        var rid = sequenceNumber.getAndIncreaseSeqNumber()
        if (rid == TEMPORAL_RID) rid = sequenceNumber.getAndIncreaseSeqNumber()

        val saveRule = Rule(
            rule.ruleInfo.copy(rid = rid),
            rule.locationTrigger?.copy(rid = rid),
            rule.timeTrigger?.copy(rid = rid),
            rule.activityTrigger?.copy(rid = rid),
            rule.appBlockAction?.copy(rid = rid),
            rule.notificationAction?.copy(rid = rid),
            rule.dndAction?.copy(rid = rid),
            rule.ringerAction?.copy(rid = rid)
        )

        ruleDao.insertRule((saveRule))
    }

    suspend fun updateRule(rule: Rule) {
        if (rule.ruleInfo.rid == TEMPORAL_RID) throw(IllegalArgumentException("RID should not be 0"))
        ruleDao.updateRule(rule)
    }
}