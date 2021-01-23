package com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "ringer_actions")
data class RingerAction(
    @PrimaryKey val rid: Int,
    @ColumnInfo val ringerMode: Int,
)