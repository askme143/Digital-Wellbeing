package com.yeongil.digitalwellbeing.viewModel.itemViewModel

import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem

interface RuleMemberItem {
    val title: String
    val description: String
    val layoutId: Int
    fun delete(recyclerItem: RecyclerItem)
}