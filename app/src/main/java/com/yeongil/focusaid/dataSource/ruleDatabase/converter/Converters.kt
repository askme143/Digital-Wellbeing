package com.yeongil.focusaid.dataSource.ruleDatabase.converter

import androidx.room.TypeConverter
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.AppBlockActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.NotificationActionEntity
import com.yeongil.focusaid.dataSource.ruleDatabase.entity.action.RingerActionEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromRepeatDayToString(value: List<Boolean>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun fromStringToRepeatDay(value: String): List<Boolean> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromRingerModeEntityToInt(value: RingerActionEntity.RingerModeEntity): Int {
        return value.ordinal
    }

    @TypeConverter
    fun fromIntToRingerModeEntity(value: Int): RingerActionEntity.RingerModeEntity {
        return enumValues<RingerActionEntity.RingerModeEntity>()[value]
    }
}