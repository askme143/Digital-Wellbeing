package com.yeongil.digitalwellbeing.viewModel.itemViewModel

import com.yeongil.digitalwellbeing.BR
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItemViewModel

class RuleMemberItemViewModel(
    val ruleMemberItem: RuleMemberItem
) : RecyclerItemViewModel {
    override fun isSameItem(other: Any): Boolean {
        if (this === other) return true
        if (javaClass != other.javaClass) return false

        other as RuleMemberItemViewModel
        return ruleMemberItem == other.ruleMemberItem
    }

    override fun isSameContent(other: Any): Boolean {
        other as RuleMemberItemViewModel
        return ruleMemberItem == other.ruleMemberItem
    }

    override fun toRecyclerItem(): RecyclerItem =
        RecyclerItem(this, ruleMemberItem.layoutId, BR.itemVM)

    fun onClickDelete() {
        ruleMemberItem.delete(toRecyclerItem())
    }

    fun onClickItem() {
        ruleMemberItem.click()
    }
}