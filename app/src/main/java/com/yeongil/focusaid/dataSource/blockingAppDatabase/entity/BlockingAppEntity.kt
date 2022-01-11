package com.yeongil.focusaid.dataSource.blockingAppDatabase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeongil.focusaid.data.BlockingApp

@Entity(tableName = "blocking_apps")
data class BlockingAppEntity(
    val rid: Int,
    @PrimaryKey @ColumnInfo(name = "package_name") val packageName: String,
    val timestamp: Long,
    @ColumnInfo(name = "allowed_time_in_seconds") val allowedTimeInSeconds: Int,
    @ColumnInfo(name = "allowed_for_this_execution") val allowedForThisExecution: Boolean,
    val action: BlockingAppActionType,
) {
    constructor(blockingApp: BlockingApp) : this(
        blockingApp.rid,
        blockingApp.packageName,
        blockingApp.timestamp,
        blockingApp.allowedTimeInSeconds,
        blockingApp.allowedForThisExecution,
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
            allowedForThisExecution,
            when (action) {
                BlockingAppActionType.CLOSE -> BlockingApp.BlockingAppActionType.CLOSE
                BlockingAppActionType.ALERT -> BlockingApp.BlockingAppActionType.ALERT
            }
        )
    }
}