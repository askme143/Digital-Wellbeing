package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.data.action.AppBlockAction
import com.yeongil.digitalwellbeing.data.action.AppBlockEntry
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem

class AppBlockActionViewModel : ViewModel() {
    val appBlockItemList = MutableLiveData<List<RecyclerItem>>()

    fun init() {
        appBlockItemList.value = listOf()
    }

    fun init(appBlockAction: AppBlockAction) {
    }

    fun appBlockItemViewModel(appBlockEntry: AppBlockEntry) {
        appBlockEntry.appName
    }
}