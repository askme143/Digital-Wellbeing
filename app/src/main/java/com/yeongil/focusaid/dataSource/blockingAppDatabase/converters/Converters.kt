package com.yeongil.focusaid.dataSource.blockingAppDatabase.converters

import androidx.room.TypeConverter
import com.yeongil.focusaid.dataSource.blockingAppDatabase.dto.BlockingAppDto.BlockingAppActionType

class Converters {
    @TypeConverter
    fun appBlockActionTypeToInt(value: BlockingAppActionType): Int {
        return value.ordinal
    }

    @TypeConverter
    fun intToAppBlockActionType(value: Int): BlockingAppActionType {
        return enumValues<BlockingAppActionType>()[value]
    }
}