package com.yeongil.focusaid.dataSource.ruleDatabase.dao.combined

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.RuleDao
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.action.AppBlockActionDao
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.action.DndActionDao
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.action.NotificationActionDao
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.action.RingerActionDao
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.trigger.ActivityTriggerDao
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.trigger.LocationTriggerDao
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.trigger.TimeTriggerDao
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.combined.RuleCombined

@Dao
interface RuleCombinedDao :
    RuleDao,
    LocationTriggerDao, TimeTriggerDao, ActivityTriggerDao,
    AppBlockActionCombinedDao, NotificationActionCombinedDao, DndActionDao, RingerActionDao {
    @Transaction
    suspend fun insertRuleCombined(ruleCombined: RuleCombined) {
        with(ruleCombined) {
            insertRule(ruleEntity)

            locationTriggerEntity?.also { insertLocationTrigger(it) }
            timeTriggerEntity?.also { insertTimeTrigger(it) }
            activityTriggerEntity?.also { insertActivityTrigger(it) }

            appBlockActionCombined?.also {
                insertAppBlockAction(it.appBlockActionEntity)
                insertAppBlockEntryList(it.appBlockEntryEntityList)
            }
            notificationActionCombined?.also {
                insertNotificationAction(it.notificationActionEntity)
                insertKeywordEntryList(it.keywordEntryEntityList)
                insertPackageNameList(it.packageNameEntityList)
            }
            dndActionEntity?.also { insertDndAction(it) }
            ringerActionEntity?.also { insertRingerAction(it) }
        }
    }

    @Transaction
    suspend fun updateRuleCombined(ruleCombined: RuleCombined) {
        with(ruleCombined) {
            updateRule(ruleEntity)

            val rid = ruleEntity.ruleId

            locationTriggerEntity?.let { insertLocationTrigger(it) }
                ?: deleteLocationTriggerByRid(rid)
            timeTriggerEntity?.let { insertTimeTrigger(it) }
                ?: deleteTimeTriggerByRid(rid)
            activityTriggerEntity?.let { insertActivityTrigger(it) }
                ?: deleteActivityTriggerByRid(rid)
            appBlockActionCombined?.let { insertAppBlockActionCombined(it) }
                ?: deleteAppBlockActionByRid(rid)
            notificationActionCombined?.let { insertNotificationActionCombined(it) }
                ?: deleteNotificationActionByRid(rid)
            dndActionEntity?.let { insertDndAction(it) }
                ?: deleteDndActionByRid(rid)
            ringerActionEntity?.let { insertRingerAction(it) }
                ?: deleteRingerActionByRid(rid)
        }
    }

    @Transaction
    suspend fun deleteRuleCombinedByRid(rid: Int) {
        deleteRuleByRid(rid)

        deleteLocationTriggerByRid(rid)
        deleteTimeTriggerByRid(rid)
        deleteActivityTriggerByRid(rid)

        deleteAppBlockActionCombinedByRid(rid)
        deleteNotificationActionCombinedByRid(rid)
        deleteDndActionByRid(rid)
        deleteRingerActionByRid(rid)
    }

    @Transaction
    @Query("SELECT * FROM rule WHERE rid = :rid")
    suspend fun getRuleCombinedByRid(rid: Int): RuleCombined

    @Transaction
    @Query("SELECT * FROM rule")
    suspend fun getRuleCombinedList(): List<RuleCombined>
}