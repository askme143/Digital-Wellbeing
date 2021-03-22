package com.yeongil.focusaid.viewModel.itemViewModel

import com.yeongil.focusaid.BR
import com.yeongil.focusaid.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.focusaid.utils.recyclerViewUtils.RecyclerItemViewModel
import com.yeongil.focusaid.viewModel.item.TriggerActionItem

// ID is Title of TriggerActionItem
class TriggerActionItemViewModel(
    override val id: String,
    private val layoutId: Int,
    val triggerActionItem: TriggerActionItem,
    val onItemClick: (String) -> Unit,
    val onClickItemDelete: (String) -> Unit
) : RecyclerItemViewModel {
    override fun isSameItem(other: Any): Boolean {
        if (this === other) return true
        if (javaClass != other.javaClass) return false

        other as TriggerActionItemViewModel
        return triggerActionItem == other.triggerActionItem
    }

    override fun isSameContent(other: Any): Boolean {
        other as TriggerActionItemViewModel
        return triggerActionItem == other.triggerActionItem
    }

    override fun toRecyclerItem(): RecyclerItem =
        RecyclerItem(this, layoutId, BR.itemVM)

    fun onClick() = onItemClick(id)
    fun onDelete() = onClickItemDelete(id)
}