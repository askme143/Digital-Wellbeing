package com.yeongil.focusaid.repository

import com.yeongil.focusaid.data.BlockingApp
import com.yeongil.focusaid.dataSource.blockingAppDatabase.dao.BlockingAppDao
import com.yeongil.focusaid.dataSource.blockingAppDatabase.entity.BlockingAppEntity

class BlockingAppRepository(
    private val blockingAppDao: BlockingAppDao
) {
    suspend fun insertBlockingApp(blockingApp: BlockingApp) =
        blockingAppDao.insertBlockingApp(BlockingAppEntity(blockingApp))

    suspend fun insertBlockingApps(blockingApps: List<BlockingApp>) =
        blockingAppDao.insertBlockingApps(blockingApps.map { BlockingAppEntity(it) })

    suspend fun updateBlockingApp(blockingApp: BlockingApp) =
        blockingAppDao.updateBlockingApp(BlockingAppEntity(blockingApp))

    suspend fun deleteAllBlockingApps() = blockingAppDao.deleteAllBlockingApps()

    suspend fun getBlockingAppByPackageName(packageName: String) =
        blockingAppDao.getBlockingAppByPackageName(packageName)?.toBlockingApp()
}