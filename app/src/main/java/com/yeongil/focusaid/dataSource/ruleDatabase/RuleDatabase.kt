package com.yeongil.focusaid.dataSource.ruleDatabase

import android.content.Context
import androidx.room.*
import com.yeongil.focusaid.dataSource.ruleDatabase.converter.Converters
import com.yeongil.focusaid.dataSource.ruleDatabase.dao.RuleDao
import com.yeongil.focusaid.dataSource.ruleDatabase.dto.action.AppBlockActionDto
import com.yeongil.focusaid.dataSource.ruleDatabase.dto.action.DndActionDto
import com.yeongil.focusaid.dataSource.ruleDatabase.dto.action.NotificationActionDto
import com.yeongil.focusaid.dataSource.ruleDatabase.dto.action.RingerActionDto
import com.yeongil.focusaid.dataSource.ruleDatabase.dto.RuleInfoDto
import com.yeongil.focusaid.dataSource.ruleDatabase.dto.trigger.ActivityTriggerDto
import com.yeongil.focusaid.dataSource.ruleDatabase.dto.trigger.LocationTriggerDto
import com.yeongil.focusaid.dataSource.ruleDatabase.dto.trigger.TimeTriggerDto

@Database(
    entities = [
        RuleInfoDto::class,
        LocationTriggerDto::class,
        ActivityTriggerDto::class,
        TimeTriggerDto::class,
        AppBlockActionDto::class,
        NotificationActionDto::class,
        DndActionDto::class,
        RingerActionDto::class,
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