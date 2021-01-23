package com.yeongil.digitalwellbeing.database.ruleDatabase

import android.content.Context
import androidx.room.*
import com.yeongil.digitalwellbeing.database.ruleDatabase.converter.Converters
import com.yeongil.digitalwellbeing.database.ruleDatabase.dao.rule.RuleDao
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.AppBlockAction
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.DndAction
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.NotificationAction
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.RingerAction
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.rule.RuleInfo
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger.ActivityTrigger
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger.LocationTrigger
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger.TimeTrigger

@Database(
    entities = [
        RuleInfo::class,
        LocationTrigger::class,
        ActivityTrigger::class,
        TimeTrigger::class,
        AppBlockAction::class,
        NotificationAction::class,
        DndAction::class,
        RingerAction::class,
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