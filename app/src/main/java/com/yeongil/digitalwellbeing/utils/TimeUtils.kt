package com.yeongil.digitalwellbeing.utils

object TimeUtils {
    private fun minutesToString(timeInMinutes: Int): String {
        val hour = timeInMinutes / 60
        val minute = timeInMinutes % 60
        val noon = if (timeInMinutes < 12) "오전" else "오후"

        return "$noon $hour:$minute"
    }

    fun startEndMinutesToString(start: Int, end: Int): String {
        val startString = minutesToString(start)
        val endString = minutesToString(end)

        return if (start < end) "$startString - $endString" else "$startString - 다음 날 $endString"
    }

    fun repeatDayToString(repeatDay: List<Boolean>): String {
        return repeatDay.mapIndexed { index, bool ->
            if (bool) {
                return when (index) {
                    SUN -> "일요일"
                    MON -> "월요일"
                    TUE -> "화요일"
                    WED -> "수요일"
                    THU -> "목요일"
                    FRI -> "금요일"
                    SAT -> "토요일"
                    else -> ""
                }
            } else ""
        }.filter { it.isNotEmpty() }.joinToString(", ")
    }
}