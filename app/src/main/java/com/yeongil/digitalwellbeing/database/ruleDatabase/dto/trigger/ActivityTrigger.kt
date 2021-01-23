package com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "activity_triggers")
data class ActivityTrigger(
    @PrimaryKey val rid: Int,
    val activity: String
)