package com.yeongil.digitalwellbeing.data.dto.action

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "dnd_actions")
data class DndAction(
    @PrimaryKey val rid: Int
)