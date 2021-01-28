package com.yeongil.digitalwellbeing.viewModel.itemViewModel

import com.yeongil.digitalwellbeing.BR
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.data.Location
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItemViewModel

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