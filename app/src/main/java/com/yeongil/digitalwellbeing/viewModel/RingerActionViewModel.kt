package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.RingerActionDto
import com.yeongil.digitalwellbeing.utils.*

class RingerActionViewModel : ViewModel() {
    private var rid = TEMPORAL_RID

    val constVibrate = VIBRATE
    val constRing = RING
    val constSilent = SILENT

    val selectedMode = MutableLiveData(constVibrate)

    fun init(rid: Int) {
        this.rid = rid
        selectedMode.value = constVibrate
    }

    fun init(ringerActionDto: RingerActionDto) {
        rid = ringerActionDto.rid
        selectedMode.value = ringerActionDto.ringerMode
    }

    fun onItemSelected(selected: Int) {
        selectedMode.value = selected
    }

    fun getRingerAction() = RingerActionDto(rid, selectedMode.value!!)
}