package com.yeongil.digitalwellbeing.repository

import com.yeongil.digitalwellbeing.data.BlockingApp
import com.yeongil.digitalwellbeing.dataSource.blockingAppDatabase.dao.BlockingAppDao
import com.yeongil.digitalwellbeing.dataSource.blockingAppDatabase.dto.BlockingAppDto

class BlockingAppRepository(
    private val blockingAppDao: BlockingAppDao
) {
    suspend fun insertBlockingApp(blockingApp: BlockingApp) =
        blockingAppDao.insertBlockingApp(BlockingAppDto(blockingApp))

    suspend fun insertBlockingApps(blockingApps: List<BlockingApp>) =
        blockingAppDao.insertBlockingApps(blockingApps.map { BlockingAppDto(it) })

    suspend fun updateBlockingApp(blockingApp: BlockingApp) =
        blockingAppDao.updateBlockingApp(BlockingAppDto(blockingApp))

    suspend fun deleteBlockingApp(packageName: String) =
        blockingAppDao.deleteBlockingApp(packageName)

    suspend fun deleteAllBlockingApps() = blockingAppDao.deleteAllBlockingApps()

    suspend fun getAllBlockingApps() =
        blockingAppDao.getAllBlockingApps().map { it.toBlockingApp() }

    suspend fun getBlockingAppByPackageName(packageName: String) =
        blockingAppDao.getBlockingAppByPackageName(packageName)?.toBlockingApp()
}