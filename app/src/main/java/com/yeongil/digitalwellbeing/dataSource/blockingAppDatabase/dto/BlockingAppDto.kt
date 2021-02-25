package com.yeongil.digitalwellbeing.dataSource.blockingAppDatabase.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeongil.digitalwellbeing.data.BlockingApp

@Entity(tableName = "blocking_apps")
data class BlockingAppDto(
    val rid: Int,
    @PrimaryKey @ColumnInfo(name = "package_name") val packageName: String,
    val timestamp: Long,
    @ColumnInfo(name = "allowed_time_in_minutes") val allowedTimeInMinutes: Int,
    val action: BlockingAppActionType,
) {
    constructor(blockingApp: BlockingApp) : this(
        blockingApp.rid,
        blockingApp.packageName,
        blockingApp.timestamp,
        blockingApp.allowedTimeInMinutes,
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
            allowedTimeInMinutes,
            when (action) {
                BlockingAppActionType.CLOSE -> BlockingApp.BlockingAppActionType.CLOSE
                BlockingAppActionType.ALERT -> BlockingApp.BlockingAppActionType.ALERT
            }
        )
    }
}