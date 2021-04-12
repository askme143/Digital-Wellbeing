package com.yeongil.focusaid.dataSource.logDatabase.dao

import androidx.room.Dao

@Dao
interface LogDao : RuleLogDao, RuleDeleteLogDao, RuleTriggerLogDao, RuleConfirmLogDao,
    RuleActivationLogDao {
}