package com.yeongil.focusaid.dataSource.ruleDatabase.entity.trigger

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.RuleEntity

@Entity(
    tableName = "activity_trigger",
    foreignKeys = [ForeignKey(
        entity = RuleEntity::class,
        parentColumns = ["rid"],
        childColumns = ["rid"],
        onDelete = CASCADE,
    )]
)
data class ActivityTriggerEntity(
    @PrimaryKey val rid: Int,
    val activity: String
)