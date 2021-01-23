package com.yeongil.digitalwellbeing.database.ruleDatabase.dao.rule

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.yeongil.digitalwellbeing.database.ruleDatabase.dao.action.*
import com.yeongil.digitalwellbeing.database.ruleDatabase.dao.trigger.ActivityTriggerDao
import com.yeongil.digitalwellbeing.database.ruleDatabase.dao.trigger.LocationTriggerDao
import com.yeongil.digitalwellbeing.database.ruleDatabase.dao.trigger.TimeTriggerDao
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.rule.Rule

@Dao
interface RuleDao :
    RuleInfoDao,
    LocationTriggerDao, TimeTriggerDao, ActivityTriggerDao,
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
    suspend fun updateRule(rule: Rule) {
        updateRuleInfo(rule.ruleInfo)

        rule.locationTrigger?.let { updateLocationTrigger(it) }
        rule.timeTrigger?.let { updateTimeTrigger(it) }
        rule.activityTrigger?.let { updateActivityTrigger(it) }

        rule.appBlockAction?.let { updateAppBlockAction(it) }
        rule.notificationAction?.let { updateNotificationAction(it) }
        rule.dndAction?.let { updateDndAction(it) }
        rule.ringerAction?.let { updateRingerAction(it) }
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

    @Transaction
    @Query("SELECT * FROM rule_info")
    suspend fun getRuleList(): List<Rule>
}