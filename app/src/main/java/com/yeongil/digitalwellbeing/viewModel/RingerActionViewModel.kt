package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.data.dto.action.RingerAction
import com.yeongil.digitalwellbeing.utils.*

class RingerActionViewModel : ViewModel() {
    private var rid = TEMPORAL_RID

    val constVibrate = VIBRATE
    val constRing = RING
    val constSilent = SILENT

    val selectedMode = MutableLiveData<Int>(constVibrate)

    fun init(rid: Int) {
        this.rid = rid
        selectedMode.value = constVibrate
    }

    fun init(ringerAction: RingerAction) {
        rid = ringerAction.rid
        selectedMode.value = ringerAction.ringerMode
    }

    fun onItemSelected(selected: Int) {
        selectedMode.value = selected
    }

    fun getRingerAction() = RingerAction(rid, selectedMode.value!!)
}