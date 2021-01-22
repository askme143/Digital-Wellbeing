package com.yeongil.digitalwellbeing.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.yeongil.digitalwellbeing.data.dto.rule.Rule
import com.yeongil.digitalwellbeing.data.dto.trigger.TimeTrigger
import com.yeongil.digitalwellbeing.utils.TEMPORAL_RID
import com.yeongil.digitalwellbeing.utils.TimeUtils.startEndMinutesToString
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.coroutineContext
import kotlin.math.min

class TimeTriggerViewModel : ViewModel() {
    private var rid = TEMPORAL_RID

    val startPickerVisible = MutableLiveData<Boolean>(true)

    val startPickerHour = MutableLiveData<Int>(0)
    val startPickerMin = MutableLiveData<Int>(0)
    val endPickerHour = MutableLiveData<Int>(0)
    val endPickerMin = MutableLiveData<Int>(0)

    val repeatDay = (1..10).map { MutableLiveData<Boolean>(false) }

    private val startTimeInMinutes = liveData<Int> {
        startPickerHour.asFlow()
            .combine(startPickerMin.asFlow()) { hour, min -> hour * 60 + min }
            .collect { emit(it) }
    }
    private val endTimeInMinutes = liveData<Int> {
        endPickerHour.asFlow()
            .combine(endPickerMin.asFlow()) { hour, min -> hour * 60 + min }
            .collect { emit(it) }
    }

    val timeText = liveData<String> {
        startTimeInMinutes.asFlow().combine(endTimeInMinutes.asFlow()) { start, end ->
            startEndMinutesToString(start, end)
        }.collect { emit(it) }
    }

    fun init(rid: Int) {
        this.rid = rid

        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMin = calendar.get(Calendar.MINUTE)
        val currentDay = calendar.get(Calendar.DAY_OF_WEEK) - 1

        setStartPickerTime(currentHour * 60 + currentMin)
        setEndPickerTime(currentHour * 60 + currentMin + 60)

        repeatDay.map { it.value = false }
        repeatDay[currentDay].value = true

        startPickerVisible.value = true
    }

    fun init(timeTrigger: TimeTrigger) {
        rid = timeTrigger.rid

        setStartPickerTime(timeTrigger.startTimeInMinutes)
        setEndPickerTime(timeTrigger.endTimeInMinutes)

        timeTrigger.repeatDay.mapIndexed { index, bool ->
            repeatDay[index].value = bool
        }

        startPickerVisible.value = true
    }

    fun onClickTabChange() {
        startPickerVisible.value = !startPickerVisible.value!!
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
            rid,
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