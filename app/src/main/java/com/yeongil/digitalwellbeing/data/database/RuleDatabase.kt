package com.yeongil.digitalwellbeing.data.database

import android.content.Context
import androidx.room.*
import com.yeongil.digitalwellbeing.data.dao.RuleDao
import com.yeongil.digitalwellbeing.data.dto.action.AppBlockAction
import com.yeongil.digitalwellbeing.data.dto.action.DndAction
import com.yeongil.digitalwellbeing.data.dto.action.NotificationAction
import com.yeongil.digitalwellbeing.data.dto.action.RingerAction
import com.yeongil.digitalwellbeing.data.dto.rule.RuleInfo
import com.yeongil.digitalwellbeing.data.dto.trigger.ActivityTrigger
import com.yeongil.digitalwellbeing.data.dto.trigger.LocationTrigger
import com.yeongil.digitalwellbeing.data.dto.trigger.TimeTrigger
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

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
        private val mutex = Mutex()

        suspend fun getInstance(context: Context): RuleDatabase {
            mutex.withLock {
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
}