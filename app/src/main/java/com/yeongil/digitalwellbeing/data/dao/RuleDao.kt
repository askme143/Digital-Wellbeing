package com.yeongil.digitalwellbeing.data.dao

import androidx.room.*
import com.yeongil.digitalwellbeing.data.dto.action.AppBlockAction
import com.yeongil.digitalwellbeing.data.dto.action.DndAction
import com.yeongil.digitalwellbeing.data.dto.action.NotificationAction
import com.yeongil.digitalwellbeing.data.dto.action.RingerAction
import com.yeongil.digitalwellbeing.data.dto.rule.Rule
import com.yeongil.digitalwellbeing.data.dto.rule.RuleInfo
import com.yeongil.digitalwellbeing.data.dto.trigger.ActivityTrigger
import com.yeongil.digitalwellbeing.data.dto.trigger.LocationTrigger
import com.yeongil.digitalwellbeing.data.dto.trigger.TimeTrigger

@Dao
interface RuleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRuleInfo(ruleInfo: RuleInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocationTrigger(locationTrigger: LocationTrigger)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeTrigger(timeTrigger: TimeTrigger)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivityTrigger(activityTrigger: ActivityTrigger)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppBlockAction(appBlockAction: AppBlockAction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotificationAction(notificationAction: NotificationAction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDndAction(dndAction: DndAction)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRingerAction(ringerAction: RingerAction)


    @Delete
    suspend fun deleteRuleInfo(ruleInfo: RuleInfo)

    @Delete
    suspend fun deleteLocationTrigger(locationTrigger: LocationTrigger)

    @Delete
    suspend fun deleteTimeTrigger(timeTrigger: TimeTrigger)

    @Delete
    suspend fun deleteActivityTrigger(activityTrigger: ActivityTrigger)

    @Delete
    suspend fun deleteAppBlockAction(appBlockAction: AppBlockAction)

    @Delete
    suspend fun deleteNotificationAction(notificationAction: NotificationAction)

    @Delete
    suspend fun deleteDndAction(dndAction: DndAction)

    @Delete
    suspend fun deleteRingerAction(ringerAction: RingerAction)

    @Transaction
    @Query("SELECT * FROM rule_info")
    suspend fun getRuleList(): List<Rule>

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
    @Query("SELECT * FROM rule_info WHERE rid = :rid")
    suspend fun getRuleWithRid(rid: Int): Rule

    @Transaction
    @Query("SELECT * FROM rule_info WHERE rule_name = :ruleName")
    suspend fun getRuleWithRuleName(ruleName: String): Rule
}