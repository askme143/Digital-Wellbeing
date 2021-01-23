package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.AppBlockActionDto
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.AppBlockEntryDto
import com.yeongil.digitalwellbeing.utils.TEMPORAL_RID
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem

class AppBlockActionViewModel : ViewModel() {
    private var rid = TEMPORAL_RID

    val appBlockItemList = MutableLiveData<List<RecyclerItem>>()

    fun init(rid: Int) {
    }
    fun init(appBlockActionDto: AppBlockActionDto) {
    }

    fun appBlockItemViewModel(appBlockEntryDto: AppBlockEntryDto) {
        appBlockEntryDto.appName
    }
}