package com.yeongil.focusaid.dataSource.blockingAppDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yeongil.focusaid.dataSource.blockingAppDatabase.converters.Converters
import com.yeongil.focusaid.dataSource.blockingAppDatabase.dao.BlockingAppDao
import com.yeongil.focusaid.dataSource.blockingAppDatabase.entity.BlockingAppEntity

@Database(
    entities = [
        BlockingAppEntity::class,
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class BlockingAppDatabase : RoomDatabase() {
    abstract fun blockingAppDao(): BlockingAppDao

    companion object {
        @Volatile
        private var INSTANCE: BlockingAppDatabase? = null

        fun getInstance(context: Context): BlockingAppDatabase {
            return INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                BlockingAppDatabase::class.java,
                "app_block_db"
            ).build().also {
                INSTANCE = it
            }
        }
    }
}