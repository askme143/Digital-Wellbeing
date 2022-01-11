package com.yeongil.focusaid.dataSource.blockingAppDatabase.dao

import androidx.room.*
import com.yeongil.focusaid.dataSource.blockingAppDatabase.entity.BlockingAppEntity

@Dao
interface BlockingAppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlockingApp(blockingApp: BlockingAppEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlockingApps(blockingApps: List<BlockingAppEntity>)

    @Update
    suspend fun updateBlockingApp(blockingApp: BlockingAppEntity)

    @Query("DELETE FROM blocking_apps WHERE package_name = :packageName")
    suspend fun deleteBlockingApp(packageName: String)

    @Query("DELETE FROM blocking_apps")
    suspend fun deleteAllBlockingApps()

    @Transaction
    @Query("SELECT * FROM blocking_apps")
    suspend fun getAllBlockingApps(): List<BlockingAppEntity>

    @Transaction
    @Query("SELECT * FROM blocking_apps WHERE package_name = :packageName")
    suspend fun getBlockingAppByPackageName(packageName: String): BlockingAppEntity?
}