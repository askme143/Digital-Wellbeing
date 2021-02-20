package com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.action

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yeongil.digitalwellbeing.data.rule.action.RingerAction
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "ringer_actions")
data class RingerActionDto(
    @PrimaryKey val rid: Int,
    @Embedded val ringerAction: RingerAction
)