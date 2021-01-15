package com.yeongil.digitalwellbeing.data.dto.action

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class RingerAction(
    @PrimaryKey val rid: Int,
    @ColumnInfo val ringerMode: Int,
)