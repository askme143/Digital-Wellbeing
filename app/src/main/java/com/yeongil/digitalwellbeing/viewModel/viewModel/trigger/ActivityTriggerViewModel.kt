package com.yeongil.digitalwellbeing.viewModel.viewModel.trigger

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.data.trigger.ActivityTrigger
import com.yeongil.digitalwellbeing.utils.BICYCLE
import com.yeongil.digitalwellbeing.utils.DRIVE
import com.yeongil.digitalwellbeing.utils.STILL

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