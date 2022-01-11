package com.yeongil.focusaid.dataSource.ruleDatabase

import android.content.Context
import androidx.room.*
import com.yeongil.focusaid.dataSource.ruleDatabase.converter.Converters
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.RuleDao
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.AppBlockActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.DndActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.NotificationActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.RingerActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.RuleInfoEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.ActivityTriggerEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.LocationTriggerEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger.TimeTriggerEntity

@Database(
    entities = [
        RuleInfoEntity::class,
        LocationTriggerEntity::class,
        ActivityTriggerEntity::class,
        TimeTriggerEntity::class,
        AppBlockActionEntity::class,
        NotificationActionEntity::class,
        DndActionEntity::class,
        RingerActionEntity::class,
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class RuleDatabase : RoomDatabase() {
    abstract fun ruleDao(): RuleDao

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