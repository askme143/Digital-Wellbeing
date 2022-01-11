package com.yeongil.focusaid.dataSource.ruleDatabase.entity.action

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeongil.focusaid.data.rule.action.DndAction
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "dnd_actions")
data class DndActionEntity(
    @PrimaryKey val rid: Int,
    @Embedded val dndAction: DndAction
)