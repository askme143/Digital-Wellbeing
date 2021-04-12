package com.yeongil.focusaid.dataSource.logDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yeongil.focusaid.dataSource.logDatabase.dao.LogDao
import com.yeongil.focusaid.dataSource.logDatabase.dto.*

@Database(
    entities = [
        RuleLogDto::class,
        RuleTriggerLogDto::class,
        RuleDeleteLogDto::class,
        RuleConfirmLogDto::class,
        RuleActivationLogDto::class
    ],
    version = 1
)
abstract class LogDatabase: RoomDatabase() {
    abstract fun logDao(): LogDao

    companion object {
        @Volatile
        private var INSTANCE: LogDatabase? = null

        fun getInstance(context: Context): LogDatabase {
            return INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                LogDatabase::class.java,
                "log_db"
            ).build().also { INSTANCE = it }
        }
    }
}