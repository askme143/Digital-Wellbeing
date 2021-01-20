package com.yeongil.digitalwellbeing.utils

import java.lang.IllegalArgumentException

object TimeUtils {
    fun minutesToString(timeInMinutes: Int): String {
        val hour = timeInMinutes / 60
        val minute = timeInMinutes % 60
        val noon = if (timeInMinutes < 12) "오전" else "오후"

        return "$noon $hour:$minute"
    }

    fun repeatDayToString(repeatDay: List<Boolean>): String {
        return repeatDay.mapIndexed { index, bool ->
            if (bool) {
                return when (index) {
                    0 -> "월요일"
                    1 -> "화요일"
                    2 -> "수요일"
                    3 -> "목요일"
                    4 -> "금요일"
                    5 -> "토요일"
                    6 -> "일요일"
                    else -> ""
                }
            } else ""
        }.filter { it.isNotEmpty() }.joinToString(", ")
    }
}