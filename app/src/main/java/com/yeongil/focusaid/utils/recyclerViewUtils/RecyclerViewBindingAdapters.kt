package com.yeongil.focusaid.utils.recyclerViewUtils

import android.graphics.Rect
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object RecyclerViewBindingAdapters {
    @BindingAdapter("recycler_item_list")
    @JvmStatic
    fun recyclerItemBind(recyclerView: RecyclerView, data: List<RecyclerItem>?) {
        if (recyclerView.adapter == null) {
            recyclerView.apply {
                if (layoutManager == null)
                    layoutManager = LinearLayoutManager(recyclerView.context)
                recyclerView.adapter = RecyclerViewAdapter()
                recyclerView.itemAnimator = null
            }
        }
        (recyclerView.adapter as RecyclerViewAdapter).submitList(data)
    }

    @BindingAdapter("recycler_item_space")
    @JvmStatic
    fun recyclerItemSpace(recyclerView: RecyclerView, bool: Boolean) {
        if (!bool) return

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)

                val adapter = parent.adapter ?: return

                if (parent.getChildAdapterPosition(view) != 0)
                    outRect.top = 10
                if (parent.getChildAdapterPosition(view) != adapter.itemCount - 1)
                    outRect.bottom = 10
            }
        })
    }

    @BindingAdapter("recycler_disable_scroll")
    @JvmStatic
    fun recyclerDisableScroll(recyclerView: RecyclerView, bool: Boolean) {
        if (!bool) return

        recyclerView.layoutManager = object : LinearLayoutManager(recyclerView.context) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
    }
}