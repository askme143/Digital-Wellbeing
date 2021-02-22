package com.yeongil.digitalwellbeing.viewModel.viewModel.action

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.data.rule.action.RingerAction
import com.yeongil.digitalwellbeing.data.rule.action.RingerMode

class RingerActionViewModel : ViewModel() {
    val selectedMode = MutableLiveData(RingerMode.VIBRATE)

    fun putRingerAction(ringerAction: RingerAction?) {
        selectedMode.value = ringerAction?.ringerMode ?: RingerMode.VIBRATE
    }

    fun onItemSelected(selected: RingerMode) {
        selectedMode.value = selected
    }

    fun getRingerAction() = RingerAction(selectedMode.value!!)
}