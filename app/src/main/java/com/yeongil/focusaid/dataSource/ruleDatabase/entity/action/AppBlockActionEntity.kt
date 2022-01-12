package com.yeongil.focusaid.dataSource.ruleDatabase.entity.action

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.RuleEntity

@Entity(
    tableName = "app_block_action",
    foreignKeys = [ForeignKey(
        entity = RuleEntity::class,
        parentColumns = ["rid"],
        childColumns = ["rid"],
        onDelete = CASCADE
    )]
)
data class AppBlockActionEntity(
    @PrimaryKey val rid: Int,
    @ColumnInfo(name = "all_app_block") val allAppBlock: Boolean,
    @ColumnInfo(name = "all_app_handling_action") val allAppHandlingAction: Int,
) {
    @Entity(
        tableName = "app_block_entry",
        foreignKeys = [ForeignKey(
            entity = AppBlockActionEntity::class,
            parentColumns = ["rid"],
            childColumns = ["rid"],
            onDelete = CASCADE
        )]
    )
    data class AppBlockEntryEntity(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Int = 0,
        @ColumnInfo(name = "rid") val rid: Int,
        @ColumnInfo(name = "package_name") val packageName: String,
        @ColumnInfo(name = "allowed_time_in_minutes") val allowedTimeInMinutes: Int,
        @ColumnInfo(name = "handling_action") val handlingAction: Int,
    )
}