package com.yeongil.digitalwellbeing.viewModel.itemViewModel

import com.yeongil.digitalwellbeing.BR
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItemViewModel
import com.yeongil.digitalwellbeing.viewModel.item.NotiAppItem

// ID is PackageName
class NotiAllAppItemViewModel(
    override val id: String,
    val onClickItemDelete: () -> Unit
) : RecyclerItemViewModel {
    override fun isSameItem(other: Any): Boolean {
        if (this === other)  return true
        if (javaClass != other.javaClass) return false

        other as NotiAllAppItemViewModel
        return id == other.id
    }

    override fun isSameContent(other: Any): Boolean {
        other as NotiAllAppItemViewModel
        return id == other.id
    }

    override fun toRecyclerItem(): RecyclerItem {
        return RecyclerItem(this, R.layout.item_noti_all_app, BR.itemVM)
    }

    fun onClickDelete() = onClickItemDelete()
}