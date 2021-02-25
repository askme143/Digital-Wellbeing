package com.yeongil.digitalwellbeing.dataSource.blockingAppDatabase.dao

import androidx.room.*
import com.yeongil.digitalwellbeing.dataSource.blockingAppDatabase.dto.BlockingAppDto
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.RuleDto

@Dao
interface BlockingAppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlockingApp(blockingApp: BlockingAppDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlockingApps(blockingApps: List<BlockingAppDto>)

    @Update
    suspend fun updateBlockingApp(blockingApp: BlockingAppDto)

    @Query("DELETE FROM blocking_apps WHERE package_name = :packageName")
    suspend fun deleteBlockingApp(packageName: String)

    @Query("DELETE FROM blocking_apps")
    suspend fun deleteAllBlockingApps()

    @Transaction
    @Query("SELECT * FROM blocking_apps")
    suspend fun getAllBlockingApps(): List<BlockingAppDto>

    @Transaction
    @Query("SELECT * FROM blocking_apps WHERE package_name = :packageName")
    suspend fun getBlockingAppByPackageName(packageName: String): BlockingAppDto?
}