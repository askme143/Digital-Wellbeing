package com.yeongil.digitalwellbeing.viewModel.viewModel.trigger

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import com.yeongil.digitalwellbeing.data.trigger.TimeTrigger
import com.yeongil.digitalwellbeing.utils.TimeUtils.startEndMinutesToString
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import java.util.*

class TimeTriggerViewModel : ViewModel() {
    val startPickerVisible = MutableLiveData(true)

    val startPickerHour = MutableLiveData(0)
    val startPickerMin = MutableLiveData(0)
    val endPickerHour = MutableLiveData(0)
    val endPickerMin = MutableLiveData(0)

    val repeatDay = (1..10).map { MutableLiveData(false) }

    private val startTimeInMinutes = liveData {
        startPickerHour.asFlow()
            .combine(startPickerMin.asFlow()) { hour, min -> hour * 60 + min }
            .collect { emit(it) }
    }
    private val endTimeInMinutes = liveData {
        endPickerHour.asFlow()
            .combine(endPickerMin.asFlow()) { hour, min -> hour * 60 + min }
            .collect { emit(it) }
    }

    val timeText = liveData {
        startTimeInMinutes.asFlow().combine(endTimeInMinutes.asFlow()) { start, end ->
            startEndMinutesToString(start, end)
        }.collect { emit(it) }
    }

    fun init() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMin = calendar.get(Calendar.MINUTE)
        val currentDay = calendar.get(Calendar.DAY_OF_WEEK) - 1

        setStartPickerTime(currentHour * 60 + currentMin)
        setEndPickerTime((currentHour * 60 + currentMin + 60) % (24 * 60))

        repeatDay.map { it.value = false }
        repeatDay[currentDay].value = true

        startPickerVisible.value = true
    }

    fun init(timeTrigger: TimeTrigger) {
        setStartPickerTime(timeTrigger.startTimeInMinutes)
        setEndPickerTime(timeTrigger.endTimeInMinutes)

        timeTrigger.repeatDay.mapIndexed { index, bool ->
            repeatDay[index].value = bool
        }

        startPickerVisible.value = true
    }

    fun onClickStartTab() {
        startPickerVisible.value = true
    }
    fun onClickFinishTab() {
        startPickerVisible.value = false
    }

    fun onClickRepeatDay(index: Int) {
        val trueDayNum = repeatDay.fold(0) { acc, day ->
            if (day.value == true) acc + 1 else acc
        }
        val dayItem = repeatDay[index]

        if (trueDayNum >= 2 || dayItem.value != true) {
            repeatDay[index].value = repeatDay[index].value != true
        }
    }

    fun getTimeTrigger() =
        TimeTrigger(
            startTimeInMinutes.value!!,
            endTimeInMinutes.value!!,
            repeatDay.map { it.value!! })

    private fun setStartPickerTime(minutes: Int) {
        startPickerHour.value = minutes / 60
        startPickerMin.value = minutes % 60
    }

    private fun setEndPickerTime(minutes: Int) {
        endPickerHour.value = minutes / 60
        endPickerMin.value = minutes % 60
    }
}