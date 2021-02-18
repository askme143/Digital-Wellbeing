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

    fun putRingerAction(ringerAction: RingerAction?) {
        selectedMode.value = ringerAction?.ringerMode ?: constVibrate
    }

    fun onItemSelected(selected: Int) {
        selectedMode.value = selected
    }

    fun getRingerAction() = RingerAction(selectedMode.value!!)
}