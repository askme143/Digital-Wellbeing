package com.yeongil.focusaid.utils

object TimeUtils {
    private fun minutesToString(timeInMinutes: Int): String {
        val (hour, noon) =
            (timeInMinutes / 60).let {
                when (it) {
                    0 -> Pair("12", "오전")
                    in 1..9 -> Pair("0$it", "오전")
                    in 10..11 -> Pair("$it", "오전")
                    12 -> Pair("12", "오후")
                    in 13..21 -> Pair("0${it - 12}", "오후")
                    else -> Pair("${it - 12}", "오후")
                }
            }
        val minute = (timeInMinutes % 60).let { if (it < 10) "0$it" else "$it" }

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
                when (index) {
                    SUN -> "일"
                    MON -> "월"
                    TUE -> "화"
                    WED -> "수"
                    THU -> "목"
                    FRI -> "금"
                    SAT -> "토"
                    else -> ""
                }
            } else ""
        }.filter { it.isNotEmpty() }.joinToString(", ")
    }

    fun minutesToTimeMinute(minutes: Int): String {
        val hour = "${minutes / 60}"
        val min = "${minutes % 60}"

        return if (hour == "0") "${min}분" else if (min == "0") "${hour}시간" else "${hour}시간 ${min}분"
    }
}