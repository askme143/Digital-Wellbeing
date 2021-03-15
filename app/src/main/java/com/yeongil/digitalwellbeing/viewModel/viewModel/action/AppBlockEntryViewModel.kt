package com.yeongil.digitalwellbeing.viewModel.viewModel.action

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.yeongil.digitalwellbeing.data.rule.action.AppBlockEntry
import com.yeongil.digitalwellbeing.utils.ALERT
import com.yeongil.digitalwellbeing.utils.CLOSE_IMMEDIATE
import com.yeongil.digitalwellbeing.utils.combineWith

class AppBlockEntryViewModel : ViewModel() {
    private companion object {
        const val ALL_APP = "ALL_APP"
    }

    /* Const */
    val constCloseImmediate = CLOSE_IMMEDIATE
    val constAlert = ALERT

    /* data */
    var packageName = ""
    val pickerHour = MutableLiveData<Int>()
    val pickerMinute = MutableLiveData<Int>()
    val handlingAction = MutableLiveData<Int>()
    fun onItemSelected(handlingAction: Int) {
        this.handlingAction.value = handlingAction
    }

    val doNotAllow = pickerHour
        .combineWith(pickerMinute) { h, m -> Pair(h, m) }
        .map { (h, m) -> h == 0 && m == 0 }
    var oldHour: Int = 0
    var oldMinute: Int = 0

    fun onDoNotAllowClick() {
        val oldValue = doNotAllow.value ?: false
        if (oldValue) {
            pickerHour.value = oldHour
            pickerMinute.value = oldMinute
        } else {
            oldHour = pickerHour.value ?: 0
            oldMinute = pickerMinute.value ?: 0
            pickerHour.value = 0
            pickerMinute.value = 0
        }
    }

    fun putAppBlockEntry(entry: AppBlockEntry) {
        packageName = entry.packageName

        pickerHour.value = entry.allowedTimeInMinutes / 60
        oldHour = entry.allowedTimeInMinutes / 60

        pickerMinute.value = entry.allowedTimeInMinutes % 60
        oldMinute = entry.allowedTimeInMinutes % 60

        handlingAction.value = entry.handlingAction
    }

    fun getAppBlockEntry(): AppBlockEntry = AppBlockEntry(
        packageName,
        pickerHour.value!! * 60 + pickerMinute.value!!,
        handlingAction.value!!
    )

    fun putAllApp(handlingAction: Int) {
        packageName = ALL_APP
        this.handlingAction.value = handlingAction
    }

    fun getAllAppHandlingAction(): Int = handlingAction.value!!
}