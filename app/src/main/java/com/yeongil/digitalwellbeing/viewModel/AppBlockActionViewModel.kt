package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.data.dto.action.AppBlockAction
import com.yeongil.digitalwellbeing.data.dto.action.AppBlockEntry
import com.yeongil.digitalwellbeing.utils.TEMPORAL_RID
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem

class AppBlockActionViewModel : ViewModel() {
    private var rid = TEMPORAL_RID

    val appBlockItemList = MutableLiveData<List<RecyclerItem>>()

    fun init(rid: Int) {
    }
    fun init(appBlockAction: AppBlockAction) {
    }

    fun appBlockItemViewModel(appBlockEntry: AppBlockEntry) {
        appBlockEntry.appName
    }
}