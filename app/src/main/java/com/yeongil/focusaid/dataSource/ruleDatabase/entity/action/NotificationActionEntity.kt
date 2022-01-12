package com.yeongil.focusaid.dataSource.ruleDatabase.entity.action

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.RuleEntity

@Entity(
    tableName = "notification_action",
    foreignKeys = [ForeignKey(
        entity = RuleEntity::class,
        parentColumns = ["rid"],
        childColumns = ["rid"],
        onDelete = CASCADE,
    )]
)
data class NotificationActionEntity(
    @PrimaryKey val rid: Int,
    @ColumnInfo(name = "all_app") val allApp: Boolean,
    @ColumnInfo(name = "handling_action") val handlingAction: Int,
) {
    @Entity(
        tableName = "keyword_entry",
        foreignKeys = [ForeignKey(
            entity = NotificationActionEntity::class,
            parentColumns = ["rid"],
            childColumns = ["rid"],
            onDelete = CASCADE
        )]
    )
    data class KeywordEntryEntity(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val rid: Int,
        val keyword: String,
        val inclusion: Boolean,
    )

    @Entity(
        tableName = "package_name",
        foreignKeys = [ForeignKey(
            entity = NotificationActionEntity::class,
            parentColumns = ["rid"],
            childColumns = ["rid"],
            onDelete = CASCADE
        )]
    )
    data class PackageNameEntity(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val rid: Int,
        @ColumnInfo(name = "package_name") val packageName: String
    )
}