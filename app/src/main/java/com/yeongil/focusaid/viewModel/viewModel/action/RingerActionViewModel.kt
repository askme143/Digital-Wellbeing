package com.yeongil.focusaid.viewModel.viewModel.action

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.focusaid.data.rule.action.RingerAction

class RingerActionViewModel : ViewModel() {
    val selectedMode = MutableLiveData(RingerAction.RingerMode.VIBRATE)

    fun putRingerAction(ringerAction: RingerAction?) {
        selectedMode.value = ringerAction?.ringerMode ?: RingerAction.RingerMode.VIBRATE
    }

    fun onItemSelected(selected: RingerAction.RingerMode) {
        selectedMode.value = selected
    }

    fun getRingerAction() = RingerAction(selectedMode.value!!)
}