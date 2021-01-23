package com.yeongil.digitalwellbeing.database.ruleDatabase.converter

import androidx.room.TypeConverter
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.AppBlockEntryDto
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.KeywordEntryDto
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
    fun fromKeywordEntryListToString(value: List<KeywordEntryDto>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun fromStringToKeywordEntryList(value: String): List<KeywordEntryDto> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromAppBlockEntryListToString(value: List<AppBlockEntryDto>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun fromStringToAppBlockEntryList(value: String): List<AppBlockEntryDto> {
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
}