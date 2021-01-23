package com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "dnd_actions")
data class DndActionDto(
    @PrimaryKey val rid: Int
)