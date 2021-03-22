package com.yeongil.focusaid.dataSource.ruleDatabase.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.action.*
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.trigger.ActivityTriggerDao
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.trigger.LocationTriggerDao
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.trigger.TimeTriggerDao
import com.yeongil.focusaid.dataSource.ruleDatabase.dto.RuleDto
import kotlinx.coroutines.flow.Flow

@Dao
interface RuleDao :
    RuleInfoDao,
    LocationTriggerDao, TimeTriggerDao, ActivityTriggerDao,
    AppBlockActionDao, NotificationActionDao, DndActionDao, RingerActionDao {
    @Transaction
    suspend fun insertRule(ruleDto: RuleDto) {
        insertRuleInfo(ruleDto.ruleInfoDto)

        ruleDto.locationTriggerDto?.let { insertLocationTrigger(it) }
        ruleDto.timeTriggerDto?.let { insertTimeTrigger(it) }
        ruleDto.activityTriggerDto?.let { insertActivityTrigger(it) }

        ruleDto.appBlockActionDto?.let { insertAppBlockAction(it) }
        ruleDto.notificationActionDto?.let { insertNotificationAction(it) }
        ruleDto.dndActionDto?.let { insertDndAction(it) }
        ruleDto.ringerActionDto?.let { insertRingerAction(it) }
    }

    @Transaction
    suspend fun updateRule(ruleDto: RuleDto) {
        updateRuleInfo(ruleDto.ruleInfoDto)

        val rid = ruleDto.ruleInfoDto.rid

        ruleDto.locationTriggerDto?.let { insertLocationTrigger(it) }
            ?: deleteLocationTriggerByRid(rid)
        ruleDto.timeTriggerDto?.let { insertTimeTrigger(it) }
            ?: deleteTimeTriggerByRid(rid)
        ruleDto.activityTriggerDto?.let { insertActivityTrigger(it) }
            ?: deleteActivityTriggerByRid(rid)
        ruleDto.appBlockActionDto?.let { insertAppBlockAction(it) }
            ?: deleteAppBlockActionByRid(rid)
        ruleDto.notificationActionDto?.let { insertNotificationAction(it) }
            ?: deleteNotificationActionByRid(rid)
        ruleDto.dndActionDto?.let { insertDndAction(it) }
            ?: deleteDndActionByRid(rid)
        ruleDto.ringerActionDto?.let { insertRingerAction(it) }
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
    suspend fun getRuleByRid(rid: Int): RuleDto

    @Transaction
    @Query("SELECT * FROM rule_info")
    fun getRuleListAsFlowByRid(): Flow<List<RuleDto>>

    @Transaction
    @Query("SELECT * FROM rule_info")
    suspend fun getRuleList(): List<RuleDto>
}