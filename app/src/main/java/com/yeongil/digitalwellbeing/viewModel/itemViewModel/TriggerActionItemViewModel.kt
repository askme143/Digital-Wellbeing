package com.yeongil.digitalwellbeing.viewModel.itemViewModel

import com.yeongil.digitalwellbeing.BR
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItemViewModel
import com.yeongil.digitalwellbeing.viewModel.item.TriggerActionItem

// ID is Title of TriggerActionItem
class TriggerActionItemViewModel(
    override val id: String,
    val layoutId: Int,
    val triggerActionItem: TriggerActionItem,
    val onClickItem: (String) -> Unit,
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

    fun onClick() = onClickItem(id)
    fun onClickDelete() = onClickItemDelete(id)
}