package com.yeongil.focusaid.dataSource.ruleDatabase.entity.action

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.RuleEntity
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "dnd_action",
    foreignKeys = [ForeignKey(
        entity = RuleEntity::class,
        parentColumns = ["rid"],
        childColumns = ["rid"],
        onDelete = CASCADE
    )]
)
data class DndActionEntity(
    @PrimaryKey val rid: Int
)