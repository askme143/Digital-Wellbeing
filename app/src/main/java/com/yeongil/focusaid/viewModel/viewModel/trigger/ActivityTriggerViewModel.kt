package com.yeongil.focusaid.viewModel.viewModel.trigger

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.focusaid.data.rule.trigger.ActivityTrigger
import com.yeongil.focusaid.utils.BICYCLE
import com.yeongil.focusaid.utils.DRIVE
import com.yeongil.focusaid.utils.STILL

class ActivityTriggerViewModel : ViewModel() {
    val constDrive = DRIVE
    val constBicycle = BICYCLE
    val constStill = STILL

    val selectedActivity = MutableLiveData<String?>()

    fun init() {
        selectedActivity.value = DRIVE
    }

    fun init(activityTrigger: ActivityTrigger) {
        selectedActivity.value = activityTrigger.activity
    }

    fun onItemSelected(selected: String) {
        selectedActivity.value = selected
    }

    fun getActivityTrigger() = ActivityTrigger(selectedActivity.value!!)
}