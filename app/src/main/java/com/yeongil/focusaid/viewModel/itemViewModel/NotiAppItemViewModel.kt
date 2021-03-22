package com.yeongil.focusaid.viewModel.itemViewModel

import com.yeongil.focusaid.BR
import com.yeongil.focusaid.R
import com.yeongil.focusaid.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.focusaid.utils.recyclerViewUtils.RecyclerItemViewModel
import com.yeongil.focusaid.viewModel.item.NotiAppItem

// ID is PackageName
class NotiAppItemViewModel(
    override val id: String,
    val notiAppItem: NotiAppItem,
    val onClickItemDelete: (String) -> Unit
) : RecyclerItemViewModel {
    override fun isSameItem(other: Any): Boolean {
        if (this === other)  return true
        if (javaClass != other.javaClass) return false

        other as NotiAppItemViewModel
        return notiAppItem == other.notiAppItem
    }

    override fun isSameContent(other: Any): Boolean {
        other as NotiAppItemViewModel
        return notiAppItem == other.notiAppItem
    }

    override fun toRecyclerItem(): RecyclerItem {
        return RecyclerItem(this, R.layout.item_noti_app, BR.itemVM)
    }

    fun onClickDelete() = onClickItemDelete(id)
}