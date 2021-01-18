package com.yeongil.digitalwellbeing.utils.recyclerViewUtils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object RecyclerViewBindingAdapters {
    @BindingAdapter("recycler_item_list")
    @JvmStatic
    fun recyclerItemBind(recyclerView: RecyclerView, data: List<RecyclerItem>?) {
        if (recyclerView.adapter == null) {
            recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
            recyclerView.adapter = RecyclerViewAdapter()
            recyclerView.itemAnimator = null
        }
        (recyclerView.adapter as RecyclerViewAdapter).submitList(data)
    }
}