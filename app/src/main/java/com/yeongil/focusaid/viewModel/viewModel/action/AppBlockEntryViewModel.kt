package com.yeongil.focusaid.viewModel.viewModel.action

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.yeongil.focusaid.data.rule.action.AppBlockEntry
import com.yeongil.focusaid.utils.ALERT
import com.yeongil.focusaid.utils.CLOSE_IMMEDIATE
import com.yeongil.focusaid.utils.combineWith

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

    fun onDoNotAllowClick() {
        pickerHour.value = 0
        pickerMinute.value = 0
    }

    fun putAppBlockEntry(entry: AppBlockEntry) {
        packageName = entry.packageName

        pickerHour.value = entry.allowedTimeInMinutes / 60
        pickerMinute.value = entry.allowedTimeInMinutes % 60

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