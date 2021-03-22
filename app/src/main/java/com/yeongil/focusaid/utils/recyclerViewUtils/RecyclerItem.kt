package com.yeongil.focusaid.utils.recyclerViewUtils

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding

data class RecyclerItem(
    val viewModel: Any,
    @LayoutRes val layoutId: Int,
    val variableId: Int
) {
    fun bind(binding: ViewDataBinding) {
        binding.setVariable(variableId, viewModel)
    }
}