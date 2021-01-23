package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger.ActivityTrigger
import com.yeongil.digitalwellbeing.utils.BICYCLE
import com.yeongil.digitalwellbeing.utils.DRIVE
import com.yeongil.digitalwellbeing.utils.STILL
import com.yeongil.digitalwellbeing.utils.TEMPORAL_RID

class ActivityTriggerViewModel : ViewModel() {
    var rid = TEMPORAL_RID

    val constDrive = DRIVE
    val constBicycle = BICYCLE
    val constStill = STILL

    val selectedActivity = MutableLiveData<String?>()

    fun init(rid: Int) {
        this.rid = rid
        selectedActivity.value = DRIVE
    }

    fun init(activityTrigger: ActivityTrigger) {
        rid = activityTrigger.rid
        selectedActivity.value = activityTrigger.activity
    }

    fun onItemSelected(selected: String) {
        selectedActivity.value = selected
    }

    fun getActivityTrigger() = ActivityTrigger(rid, selectedActivity.value!!)
}