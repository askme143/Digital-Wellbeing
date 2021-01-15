package com.yeongil.digitalwellbeing.data.database

import androidx.room.TypeConverter
import com.yeongil.digitalwellbeing.data.dto.action.AppBlockEntry
import com.yeongil.digitalwellbeing.data.dto.action.KeywordEntry
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
        return Json.decodeFromString<List<String>>(value)
    }

    @TypeConverter
    fun fromKeywordEntryListToString(value: List<KeywordEntry>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun fromStringToKeywordEntryList(value: String): List<KeywordEntry> {
        return Json.decodeFromString<List<KeywordEntry>>(value)
    }

    @TypeConverter
    fun fromAppBlockEntryListToString(value: List<AppBlockEntry>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun fromStringToAppBlockEntryList(value: String): List<AppBlockEntry> {
        return Json.decodeFromString<List<AppBlockEntry>>(value)
    }
}