package com.yeongil.digitalwellbeing.data.dao

import androidx.room.*
import com.yeongil.digitalwellbeing.data.dao.action.AppBlockActionDao
import com.yeongil.digitalwellbeing.data.dao.action.DndActionDao
import com.yeongil.digitalwellbeing.data.dao.action.NotificationActionDao
import com.yeongil.digitalwellbeing.data.dao.action.RingerActionDao
import com.yeongil.digitalwellbeing.data.dao.rule.RuleInfoDao
import com.yeongil.digitalwellbeing.data.dao.trigger.ActivityTriggerDao
import com.yeongil.digitalwellbeing.data.dao.trigger.LocationTriggerDao
import com.yeongil.digitalwellbeing.data.dao.trigger.TimeTriggerDao
import com.yeongil.digitalwellbeing.data.dto.rule.Rule

@Dao
interface RuleDao : RuleInfoDao, LocationTriggerDao, TimeTriggerDao, ActivityTriggerDao,
    AppBlockActionDao, NotificationActionDao, DndActionDao, RingerActionDao {
    @Transaction
    suspend fun insertRule(rule: Rule) {
        insertRuleInfo(rule.ruleInfo)

        rule.locationTrigger?.let { insertLocationTrigger(it) }
        rule.timeTrigger?.let { insertTimeTrigger(it) }
        rule.activityTrigger?.let { insertActivityTrigger(it) }

        rule.appBlockAction?.let { insertAppBlockAction(it) }
        rule.notificationAction?.let { insertNotificationAction(it) }
        rule.dndAction?.let { insertDndAction(it) }
        rule.ringerAction?.let { insertRingerAction(it) }
    }

    @Transaction
    @Query("SELECT * FROM rule_info")
    suspend fun getRuleList(): List<Rule>

    @Transaction
    @Query("SELECT * FROM rule_info WHERE rid = :rid")
    suspend fun getRuleWithRid(rid: Int): Rule

    @Transaction
    @Query("SELECT * FROM rule_info WHERE rule_name = :ruleName")
    suspend fun getRuleWithRuleName(ruleName: String): Rule

    @Transaction
    suspend fun deleteRule(rule: Rule) {
        deleteRuleInfo(rule.ruleInfo)

        rule.locationTrigger?.let { deleteLocationTrigger(it) }
        rule.timeTrigger?.let { deleteTimeTrigger(it) }
        rule.activityTrigger?.let { deleteActivityTrigger(it) }

        rule.appBlockAction?.let { deleteAppBlockAction(it) }
        rule.notificationAction?.let { deleteNotificationAction(it) }
        rule.dndAction?.let { deleteDndAction(it) }
        rule.ringerAction?.let { deleteRingerAction(it) }
    }

    @Transaction
    suspend fun deleteRuleByRid(rid: Int) {
        deleteRuleInfoByRid(rid)

        deleteLocationTriggerByRid(rid)
        deleteTimeTriggerByRid(rid)
        deleteActivityTriggerByRid(rid)

        deleteAppBlockActionByRid(rid)
        deleteNotificationActionByRid(rid)
        deleteDndActionByRid(rid)
        deleteRingerActionByRid(rid)
    }
}