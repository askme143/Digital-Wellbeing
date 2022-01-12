package com.yeongil.focusaid.dataSource.ruleDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yeongil.focusaid.dataSource.ruleDatabase.converter.Converters
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.combined.RuleCombinedDao
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.RuleEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.AppBlockActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.DndActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.NotificationActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.RingerActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.combined.RuleCombined
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.ActivityTriggerEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.LocationTriggerEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.TimeTriggerEntity

@Database(
    entities = [
        RuleEntity::class,
        LocationTriggerEntity::class,
        ActivityTriggerEntity::class,
        TimeTriggerEntity::class,
        AppBlockActionEntity::class,
        AppBlockActionEntity.AppBlockEntryEntity::class,
        NotificationActionEntity::class,
        NotificationActionEntity.KeywordEntryEntity::class,
        NotificationActionEntity.PackageNameEntity::class,
        DndActionEntity::class,
        RingerActionEntity::class,
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class RuleDatabase : RoomDatabase() {
    abstract fun ruleCombinedDao(): RuleCombinedDao

    companion object {
        @Volatile
        private var INSTANCE: RuleDatabase? = null

        fun getInstance(context: Context): RuleDatabase {
            return INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                RuleDatabase::class.java,
                "rule_db"
            ).build().also {
                INSTANCE = it
            }
        }
    }
}