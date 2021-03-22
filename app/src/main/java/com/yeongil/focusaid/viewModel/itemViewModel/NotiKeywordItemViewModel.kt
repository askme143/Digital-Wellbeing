package com.yeongil.focusaid.viewModel.itemViewModel

import com.yeongil.focusaid.BR
import com.yeongil.focusaid.R
import com.yeongil.focusaid.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.focusaid.utils.recyclerViewUtils.RecyclerItemViewModel
import com.yeongil.focusaid.viewModel.item.NotiKeywordItem

// ID is EpochTime (or List Index of initialized item view models)
class NotiKeywordItemViewModel(
    override val id: String,
    val notiKeywordItem: NotiKeywordItem,
    val onClickItem: (String) -> Unit,
    val onClickItemDelete: (String) -> Unit,
) : RecyclerItemViewModel {
    override fun isSameItem(other: Any): Boolean {
        if (this === other) return true
        if (javaClass != other.javaClass) return false

        other as NotiKeywordItemViewModel
        return this.notiKeywordItem == other.notiKeywordItem
    }

    override fun isSameContent(other: Any): Boolean {
        other as NotiKeywordItemViewModel
        return this.notiKeywordItem == other.notiKeywordItem
    }

    override fun toRecyclerItem(): RecyclerItem {
        return RecyclerItem(this, R.layout.item_noti_keyword, BR.itemVM)
    }

    fun onClick() = onClickItem(id)
    fun onClickDelete() = onClickItemDelete(id)
}