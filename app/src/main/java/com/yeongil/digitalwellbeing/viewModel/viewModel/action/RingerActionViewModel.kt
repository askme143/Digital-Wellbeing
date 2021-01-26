package com.yeongil.digitalwellbeing.viewModel.viewModel.action

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.data.action.RingerAction
import com.yeongil.digitalwellbeing.utils.*

class RingerActionViewModel : ViewModel() {
    val constVibrate = VIBRATE
    val constRing = RING
    val constSilent = SILENT

    val selectedMode = MutableLiveData(constVibrate)

    fun init() {
        selectedMode.value = constVibrate
    }

    fun init(ringerAction: RingerAction) {
        selectedMode.value = ringerAction.ringerMode
    }

    fun onItemSelected(selected: Int) {
        selectedMode.value = selected
    }

    fun getRingerAction() = RingerAction(selectedMode.value!!)
}