package com.yeongil.digitalwellbeing.viewModel.itemViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.yeongil.digitalwellbeing.BR
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItemViewModel
import com.yeongil.digitalwellbeing.viewModel.item.AppItem

// ID is PackageName
class AppItemViewModel(
    override val id: String,
    val appItem: AppItem,
    val isAllAppClicked: MutableLiveData<Boolean>,
    val onClickItem: (MutableLiveData<Boolean>) -> Unit,
) : RecyclerItemViewModel {
    override fun isSameItem(other: Any): Boolean {
        if (this === other) return true
        if (javaClass != other.javaClass) return false

        other as AppItemViewModel
        return appItem == other.appItem
    }

    override fun isSameContent(other: Any): Boolean {
        other as AppItemViewModel
        return appItem == other.appItem
    }

    override fun toRecyclerItem(): RecyclerItem {
        return RecyclerItem(this, R.layout.item_app, BR.itemVM)
    }

    fun onClickItem() {
        onClickItem(appItem.checked)
    }
}