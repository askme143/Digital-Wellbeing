package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.data.action.AppBlockEntry
import com.yeongil.digitalwellbeing.utils.ALERT
import com.yeongil.digitalwellbeing.utils.CLOSE_IMMEDIATE

class AppBlockEntryViewModel : ViewModel() {
    val constCloseImmediate = CLOSE_IMMEDIATE
    val constAlert = ALERT

    var packageName = ""

    val pickerHour = MutableLiveData<Int>()
    val pickerMinute = MutableLiveData<Int>()
    val handlingAction = MutableLiveData<Int>()
    fun onItemSelected(handlingAction: Int) = run { this.handlingAction.value = handlingAction }


    fun init(appBlockEntry: AppBlockEntry) {
        packageName = appBlockEntry.packageName
        pickerHour.value = appBlockEntry.allowedTimeInMinutes / 60
        pickerMinute.value = appBlockEntry.allowedTimeInMinutes % 60
        handlingAction.value = appBlockEntry.handlingAction
    }

    fun getAppBlockEntry() = AppBlockEntry(
        packageName,
        pickerHour.value!! * 60 + pickerMinute.value!!,
        handlingAction.value!!
    )
}