package com.yeongil.focusaid.viewModel.itemViewModel

import com.yeongil.focusaid.BR
import com.yeongil.focusaid.R
import com.yeongil.focusaid.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.focusaid.utils.recyclerViewUtils.RecyclerItemViewModel

class AllAppBlockEntryItemViewModel(
    override val id: String = "ALL_APP",
    val description: String,
    val isFirstClick: Boolean,
    val onClickItem: () -> Unit,
    val onClickDeleteItem: () -> Unit
) : RecyclerItemViewModel {
    override fun isSameItem(other: Any): Boolean {
        if (this === other) return true
        if (javaClass != other.javaClass) return false

        other as AllAppBlockEntryItemViewModel
        return this.description == other.description && this.isFirstClick == other.isFirstClick
    }

    override fun isSameContent(other: Any): Boolean {
        other as AllAppBlockEntryItemViewModel
        return this.description == other.description && this.isFirstClick == other.isFirstClick
    }

    override fun toRecyclerItem(): RecyclerItem {
        return RecyclerItem(this, R.layout.item_all_app_block_entry, BR.itemVM)
    }

    fun onClick() {
        onClickItem()
    }

    fun onClickDelete() {
        onClickDeleteItem()
    }
}