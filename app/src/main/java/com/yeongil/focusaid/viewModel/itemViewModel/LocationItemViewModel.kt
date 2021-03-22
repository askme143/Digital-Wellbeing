package com.yeongil.focusaid.viewModel.itemViewModel

import com.yeongil.focusaid.BR
import com.yeongil.focusaid.R
import com.yeongil.focusaid.data.Location
import com.yeongil.focusaid.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.focusaid.utils.recyclerViewUtils.RecyclerItemViewModel

class LocationItemViewModel(
    override val id: String,
    val location: Location,
    val onClickItem: (String) -> Unit
) :
    RecyclerItemViewModel {
    override fun isSameItem(other: Any): Boolean {
        if (this === other) return true
        if (javaClass != other.javaClass) return false

        other as LocationItemViewModel
        return location == other.location
    }

    override fun isSameContent(other: Any): Boolean {
        other as LocationItemViewModel
        return location == other.location
    }

    override fun toRecyclerItem(): RecyclerItem {
        return RecyclerItem(this, R.layout.item_location, BR.itemVM)
    }

    fun onClickItem() = onClickItem(id)
}