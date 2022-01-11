package com.yeongil.focusaid.dataSource.blockingAppDatabase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "blocking_apps")
data class BlockingAppEntity(
    val rid: Int,
    @PrimaryKey @ColumnInfo(name = "package_name") val packageName: String,
    val timestamp: Long,
    @ColumnInfo(name = "allowed_time_in_seconds") val allowedTimeInSeconds: Int,
    @ColumnInfo(name = "allowed_for_this_execution") val allowedForThisExecution: Boolean,
    val action: BlockingAppActionTypeEntity,
) {
    enum class BlockingAppActionTypeEntity { CLOSE, ALERT }
}