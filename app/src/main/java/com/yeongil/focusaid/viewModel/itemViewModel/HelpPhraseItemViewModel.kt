package com.yeongil.focusaid.viewModel.itemViewModel

import com.yeongil.focusaid.BR
import com.yeongil.focusaid.R
import com.yeongil.focusaid.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.focusaid.utils.recyclerViewUtils.RecyclerItemViewModel

class HelpPhraseItemViewModel(val text: String) : RecyclerItemViewModel {
    override val id = "RULE_COUNT_ITEM_VIEW_MODEL"
    override fun isSameItem(other: Any): Boolean {
        if (this === other) return true
        if (javaClass != other.javaClass) return false

        other as HelpPhraseItemViewModel
        return this.text == other.text
    }

    override fun isSameContent(other: Any): Boolean {
        other as HelpPhraseItemViewModel
        return this.text == other.text
    }

    override fun toRecyclerItem(): RecyclerItem {
        return RecyclerItem(this, R.layout.item_help_phrase, BR.itemVM)
    }
}