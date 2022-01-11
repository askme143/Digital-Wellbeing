package com.yeongil.focusaid.repository

import com.yeongil.focusaid.data.BlockingApp
import com.yeongil.focusaid.dataSource.blockingAppDatabase.dao.BlockingAppDao
import com.yeongil.focusaid.dataSource.blockingAppDatabase.entity.BlockingAppEntity
import com.yeongil.focusaid.mapper.toData
import com.yeongil.focusaid.mapper.toEntity

class BlockingAppRepository(
    private val blockingAppDao: BlockingAppDao
) {
    suspend fun insertBlockingApp(blockingApp: BlockingApp) =
        blockingAppDao.insertBlockingApp(blockingApp.toEntity())

    suspend fun insertBlockingApps(blockingApps: List<BlockingApp>) =
        blockingAppDao.insertBlockingApps(blockingApps.map { it.toEntity() })

    suspend fun updateBlockingApp(blockingApp: BlockingApp) =
        blockingAppDao.updateBlockingApp(blockingApp.toEntity())

    suspend fun deleteAllBlockingApps() = blockingAppDao.deleteAllBlockingApps()

    suspend fun getBlockingAppByPackageName(packageName: String) =
        blockingAppDao.getBlockingAppByPackageName(packageName)?.toData()
}