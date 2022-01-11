package com.yeongil.focusaid.dataSource.blockingAppDatabase.converters

import androidx.room.TypeConverter
import com.yeongil.focusaid.dataSource.blockingAppDatabase.entity.BlockingAppEntity.BlockingAppActionTypeEntity

class Converters {
    @TypeConverter
    fun appBlockActionTypeToInt(value: BlockingAppActionTypeEntity): Int {
        return value.ordinal
    }

    @TypeConverter
    fun intToAppBlockActionType(value: Int): BlockingAppActionTypeEntity {
        return enumValues<BlockingAppActionTypeEntity>()[value]
    }
}