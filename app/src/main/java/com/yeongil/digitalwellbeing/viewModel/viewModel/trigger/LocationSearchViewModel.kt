package com.yeongil.digitalwellbeing.viewModel.viewModel.trigger

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem

class LocationSearchViewModel: ViewModel() {
    // TODO: Make Location Item View Model
    val locationRecyclerItemList = MutableLiveData<RecyclerItem>()
}