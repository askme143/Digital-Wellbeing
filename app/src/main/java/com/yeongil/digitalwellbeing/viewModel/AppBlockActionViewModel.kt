package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.data.action.AppBlockAction
import com.yeongil.digitalwellbeing.data.action.AppBlockEntry
import com.yeongil.digitalwellbeing.repository.PackageManagerRepository
import com.yeongil.digitalwellbeing.utils.Event
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItemViewModel
import com.yeongil.digitalwellbeing.viewModel.item.AppItem
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.AppItemViewModel

class AppBlockActionViewModel(
    private val pmRepo: PackageManagerRepository
) : ViewModel() {
    val appBlockItemList = MutableLiveData<List<RecyclerItem>>()

    fun init() {
        appBlockItemList.value = listOf()
    }

    fun init(appBlockAction: AppBlockAction) {
    }
}