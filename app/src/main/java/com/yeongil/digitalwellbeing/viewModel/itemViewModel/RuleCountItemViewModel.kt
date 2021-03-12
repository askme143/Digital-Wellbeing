package com.yeongil.digitalwellbeing.viewModel.itemViewModel

import com.yeongil.digitalwellbeing.BR
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItemViewModel

class RuleCountItemViewModel(private val count: Int) : RecyclerItemViewModel {
    override val id = "RULE_COUNT_ITEM_VIEW_MODEL"
    override fun isSameItem(other: Any): Boolean {
        if (this === other) return true
        if (javaClass != other.javaClass) return false

        other as RuleCountItemViewModel
        return this.count == other.count
    }

    override fun isSameContent(other: Any): Boolean {
        other as RuleCountItemViewModel
        return this.count == other.count
    }

    override fun toRecyclerItem(): RecyclerItem {
        return RecyclerItem(this, R.layout.item_rule_count, BR.itemVM)
    }

    val text = "현재 ${count}개의 방해 관리\n 규칙이 있습니다."
}