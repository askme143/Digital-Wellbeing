package com.yeongil.focusaid.dataSource.ruleDatabase.entity.action

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "ringer_actions")
data class RingerActionEntity(
    @PrimaryKey val rid: Int,
    @ColumnInfo(name = "ringer_mode") val ringerModeEntity: RingerModeEntity,
) {
    @Parcelize
    enum class RingerModeEntity : Parcelable { VIBRATE, RING, SILENT }
}