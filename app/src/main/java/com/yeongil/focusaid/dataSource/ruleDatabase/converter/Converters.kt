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
    fun fromAppListToString(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun fromStringToAppList(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromKeywordEntryEntityListToString(value: List<NotificationActionEntity.KeywordEntryEntity>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun fromStringToKeywordEntryEntityList(value: String): List<NotificationActionEntity.KeywordEntryEntity> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromAppBlockEntryEntityListToString(value: List<AppBlockActionEntity.AppBlockEntryEntity>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun fromStringToAppBlockEntryEntityList(value: String): List<AppBlockActionEntity.AppBlockEntryEntity> {
        return Json.decodeFromString(value)
    }

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