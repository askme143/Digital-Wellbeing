package com.yeongil.digitalwellbeing.viewModel.itemViewModel

import com.yeongil.digitalwellbeing.BR
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItemViewModel
import com.yeongil.digitalwellbeing.viewModel.item.AppBlockEntryItem

class AppBlockEntryItemViewModel(
    override val id: String,
    val appBlockEntryItem: AppBlockEntryItem,
    val onClickItem: (String) -> Unit,
    val onClickItemDelete: (String) -> Unit,
) : RecyclerItemViewModel {
    override fun isSameItem(other: Any): Boolean {
        if (this === other) return true
        if (javaClass != other.javaClass) return false

        other as AppBlockEntryItemViewModel
        return appBlockEntryItem == other.appBlockEntryItem
    }

    override fun isSameContent(other: Any): Boolean {
        other as AppBlockEntryItemViewModel
        return appBlockEntryItem == other.appBlockEntryItem
    }

    override fun toRecyclerItem(): RecyclerItem {
        return RecyclerItem(this, R.layout.item_app_block_entry, BR.itemVM)
    }

    fun onClick() = onClickItem(id)
    fun onClickDelete() = onClickItemDelete(id)
}