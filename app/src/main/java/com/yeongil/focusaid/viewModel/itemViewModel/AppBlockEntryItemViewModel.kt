package com.yeongil.focusaid.viewModel.itemViewModel

import com.yeongil.focusaid.BR
import com.yeongil.focusaid.R
import com.yeongil.focusaid.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.focusaid.utils.recyclerViewUtils.RecyclerItemViewModel
import com.yeongil.focusaid.viewModel.item.AppBlockEntryItem

// ID is PackageName
class AppBlockEntryItemViewModel(
    override val id: String,
    val appBlockEntryItem: AppBlockEntryItem,
    val isFirstClick: Boolean,
    val onClickItem: (String) -> Unit,
    val onClickItemDelete: (String) -> Unit,
) : RecyclerItemViewModel {
    override fun isSameItem(other: Any): Boolean {
        if (this === other) return true
        if (javaClass != other.javaClass) return false

        other as AppBlockEntryItemViewModel
        return appBlockEntryItem == other.appBlockEntryItem && isFirstClick == other.isFirstClick
    }

    override fun isSameContent(other: Any): Boolean {
        other as AppBlockEntryItemViewModel
        return appBlockEntryItem == other.appBlockEntryItem && isFirstClick == other.isFirstClick
    }

    override fun toRecyclerItem(): RecyclerItem {
        return RecyclerItem(this, R.layout.item_app_block_entry, BR.itemVM)
    }

    fun onClick() = onClickItem(id)
    fun onClickDelete() = onClickItemDelete(id)
}