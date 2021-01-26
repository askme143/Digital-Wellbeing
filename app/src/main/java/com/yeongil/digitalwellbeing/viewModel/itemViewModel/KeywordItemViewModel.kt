package com.yeongil.digitalwellbeing.viewModel.itemViewModel

import com.yeongil.digitalwellbeing.BR
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItemViewModel
import com.yeongil.digitalwellbeing.viewModel.item.KeywordItem

class KeywordItemViewModel(
    override val id: String,
    val keywordItem: KeywordItem,
    val onClickItem: (String) -> Unit,
    val onClickItemDelete: (String) -> Unit,
) : RecyclerItemViewModel {
    override fun isSameItem(other: Any): Boolean {
        if (this === other) return true
        if (javaClass != other.javaClass) return false

        other as KeywordItemViewModel
        return this.keywordItem == other.keywordItem
    }

    override fun isSameContent(other: Any): Boolean {
        other as KeywordItemViewModel
        return this.keywordItem == other.keywordItem
    }

    override fun toRecyclerItem(): RecyclerItem {
        return RecyclerItem(this, R.layout.item_keyword, BR.itemVM)
    }

    fun onClick() = onClickItem(id)
    fun onClickDelete() = onClickItemDelete(id)
}