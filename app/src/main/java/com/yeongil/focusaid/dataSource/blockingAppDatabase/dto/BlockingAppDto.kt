package com.yeongil.focusaid.dataSource.blockingAppDatabase.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeongil.focusaid.data.BlockingApp

@Entity(tableName = "blocking_apps")
data class BlockingAppDto(
    val rid: Int,
    @PrimaryKey @ColumnInfo(name = "package_name") val packageName: String,
    val timestamp: Long,
    @ColumnInfo(name = "allowed_time_in_seconds") val allowedTimeInSeconds: Int,
    val action: BlockingAppActionType,
) {
    constructor(blockingApp: BlockingApp) : this(
        blockingApp.rid,
        blockingApp.packageName,
        blockingApp.timestamp,
        blockingApp.allowedTimeInSeconds,
        when (blockingApp.action) {
            BlockingApp.BlockingAppActionType.CLOSE -> BlockingAppActionType.CLOSE
            BlockingApp.BlockingAppActionType.ALERT -> BlockingAppActionType.ALERT
        }
    )

    enum class BlockingAppActionType { CLOSE, ALERT }

    fun toBlockingApp(): BlockingApp {
        return BlockingApp(
            rid,
            packageName,
            timestamp,
            allowedTimeInSeconds,
            when (action) {
                BlockingAppActionType.CLOSE -> BlockingApp.BlockingAppActionType.CLOSE
                BlockingAppActionType.ALERT -> BlockingApp.BlockingAppActionType.ALERT
            }
        )
    }
}