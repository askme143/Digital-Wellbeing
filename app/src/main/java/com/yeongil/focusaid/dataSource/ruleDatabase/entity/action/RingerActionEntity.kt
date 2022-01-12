package com.yeongil.focusaid.dataSource.ruleDatabase.entity.action

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.RuleEntity
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "ringer_action",
    foreignKeys = [ForeignKey(
        entity = RuleEntity::class,
        parentColumns = ["rid"],
        childColumns = ["rid"],
        onDelete = CASCADE,
    )]
)
data class RingerActionEntity(
    @PrimaryKey val rid: Int,
    @ColumnInfo(name = "ringer_mode") val ringerModeEntity: RingerModeEntity,
) {
    @Parcelize
    enum class RingerModeEntity : Parcelable { VIBRATE, RING, SILENT }
}