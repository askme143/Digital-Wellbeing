package com.yeongil.digitalwellbeing.database.ruleDatabase

import android.content.Context
import androidx.room.*
import com.yeongil.digitalwellbeing.database.ruleDatabase.converter.Converters
import com.yeongil.digitalwellbeing.database.ruleDatabase.dao.rule.RuleDao
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.AppBlockActionDto
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.DndActionDto
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.NotificationAction
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.RingerActionDto
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.rule.RuleInfoDto
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger.ActivityTriggerDto
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger.LocationTriggerDto
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger.TimeTriggerDto

@Database(
    entities = [
        RuleInfoDto::class,
        LocationTriggerDto::class,
        ActivityTriggerDto::class,
        TimeTriggerDto::class,
        AppBlockActionDto::class,
        NotificationAction::class,
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