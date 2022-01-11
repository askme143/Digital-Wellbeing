package com.yeongil.focusaid.dataSource.ruleDatabase.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.action.*
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.trigger.ActivityTriggerDao
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.trigger.LocationTriggerDao
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.trigger.TimeTriggerDao
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.RuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RuleDao :
    RuleInfoDao,
    LocationTriggerDao, TimeTriggerDao, ActivityTriggerDao,
    AppBlockActionDao, NotificationActionDao, DndActionDao, RingerActionDao {
    @Transaction
    suspend fun insertRule(ruleEntity: RuleEntity) {
        insertRuleInfo(ruleEntity.ruleInfoEntity)

        ruleEntity.locationTriggerEntity?.let { insertLocationTrigger(it) }
        ruleEntity.timeTriggerEntity?.let { insertTimeTrigger(it) }
        ruleEntity.activityTriggerEntity?.let { insertActivityTrigger(it) }

        ruleEntity.appBlockActionEntity?.let { insertAppBlockAction(it) }
        ruleEntity.notificationActionEntity?.let { insertNotificationAction(it) }
        ruleEntity.dndActionEntity?.let { insertDndAction(it) }
        ruleEntity.ringerActionEntity?.let { insertRingerAction(it) }
    }

    @Transaction
    suspend fun updateRule(ruleEntity: RuleEntity) {
        updateRuleInfo(ruleEntity.ruleInfoEntity)

        val rid = ruleEntity.ruleInfoEntity.rid

        ruleEntity.locationTriggerEntity?.let { insertLocationTrigger(it) }
            ?: deleteLocationTriggerByRid(rid)
        ruleEntity.timeTriggerEntity?.let { insertTimeTrigger(it) }
            ?: deleteTimeTriggerByRid(rid)
        ruleEntity.activityTriggerEntity?.let { insertActivityTrigger(it) }
            ?: deleteActivityTriggerByRid(rid)
        ruleEntity.appBlockActionEntity?.let { insertAppBlockAction(it) }
            ?: deleteAppBlockActionByRid(rid)
        ruleEntity.notificationActionEntity?.let { insertNotificationAction(it) }
            ?: deleteNotificationActionByRid(rid)
        ruleEntity.dndActionEntity?.let { insertDndAction(it) }
            ?: deleteDndActionByRid(rid)
        ruleEntity.ringerActionEntity?.let { insertRingerAction(it) }
            ?: deleteRingerActionByRid(rid)
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
    @Query("SELECT * FROM rule_info WHERE rid = :rid")
    suspend fun getRuleByRid(rid: Int): RuleEntity

    @Transaction
    @Query("SELECT * FROM rule_info")
    fun getRuleListAsFlowByRid(): Flow<List<RuleEntity>>

    @Transaction
    @Query("SELECT * FROM rule_info")
    suspend fun getRuleList(): List<RuleEntity>
}