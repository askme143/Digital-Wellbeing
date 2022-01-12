package com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.RuleEntity

@Entity(
    tableName = "time_trigger",
    foreignKeys = [ForeignKey(
        entity = RuleEntity::class,
        parentColumns = ["rid"],
        childColumns = ["rid"],
        onDelete = CASCADE,
    )]
)
data class TimeTriggerEntity(
    @PrimaryKey val rid: Int,
    @ColumnInfo(name = "start_time_in_minutes") val startTimeInMinutes: Int,
    @ColumnInfo(name = "end_time_in_minutes") val endTimeInMinutes: Int,
    @ColumnInfo(name = "repeat_day") val repeatDay: List<Boolean>,
)