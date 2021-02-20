package com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.action

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeongil.digitalwellbeing.data.rule.action.DndAction
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "dnd_actions")
data class DndActionDto(
    @PrimaryKey val rid: Int,
    @Embedded val dndAction: DndAction
)