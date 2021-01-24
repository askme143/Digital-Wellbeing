package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.data.action.AppBlockEntry
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.action.AppBlockActionDto
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem

class AppBlockActionViewModel : ViewModel() {
    val appBlockItemList = MutableLiveData<List<RecyclerItem>>()

    fun init() {
    }
    fun init(appBlockActionDto: AppBlockActionDto) {
    }

    fun appBlockItemViewModel(appBlockEntry: AppBlockEntry) {
        appBlockEntry.appName
    }
}