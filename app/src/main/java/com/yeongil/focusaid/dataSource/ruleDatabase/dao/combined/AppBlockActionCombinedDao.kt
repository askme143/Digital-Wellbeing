package com.yeongil.focusaid.dataSource.ruleDatabase.dao.combined

import androidx.room.*
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.action.AppBlockActionDao
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.combined.AppBlockActionCombined

@Dao
interface AppBlockActionCombinedDao : AppBlockActionDao {
    @Transaction
    suspend fun insertAppBlockActionCombined(appBlockActionCombined: AppBlockActionCombined) {
        with(appBlockActionCombined) {
            insertAppBlockAction(appBlockActionEntity)
            insertAppBlockEntryList(appBlockEntryEntityList)
        }
    }

    @Transaction
    suspend fun updateAppBlockActionCombined(appBlockActionCombined: AppBlockActionCombined) {
        with(appBlockActionCombined) {
            updateAppBlockAction(appBlockActionEntity)
            deleteAppBlockEntryListByRid(appBlockActionEntity.rid)
            insertAppBlockEntryList(appBlockEntryEntityList)
        }
    }

    @Query("DELETE FROM app_block_action WHERE rid = :rid")
    suspend fun deleteAppBlockActionCombinedByRid(rid: Int) {
        deleteAppBlockActionByRid(rid)
        deleteAppBlockEntryListByRid(rid)
    }
}