package com.yeongil.digitalwellbeing.viewModel.itemViewModel

import com.yeongil.digitalwellbeing.BR
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItemViewModel

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