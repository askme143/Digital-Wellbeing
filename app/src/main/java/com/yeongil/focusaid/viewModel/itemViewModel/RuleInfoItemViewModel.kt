package com.yeongil.focusaid.viewModel.itemViewModel

import com.yeongil.focusaid.BR
import com.yeongil.focusaid.R
import com.yeongil.focusaid.data.rule.RuleInfo
import com.yeongil.focusaid.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.focusaid.utils.recyclerViewUtils.RecyclerItemViewModel

// ID is RuleID
class RuleInfoItemViewModel(
    override val id: String,
    val ruleInfo: RuleInfo,
    private val onClickActivate: (RuleInfo) -> Unit,
    private val onClickNotiOnTrigger: (RuleInfo) -> Unit,
    private val onClickDelete: (Int, String) -> Unit,
    private val onClickItem: (Int) -> Unit,
) :
    RecyclerItemViewModel {
    override fun isSameItem(other: Any): Boolean {
        if (this === other) return true
        if (javaClass != other.javaClass) return false

        other as RuleInfoItemViewModel
        return this.ruleInfo == other.ruleInfo
    }

    override fun isSameContent(other: Any): Boolean {
        other as RuleInfoItemViewModel
        return this.ruleInfo == other.ruleInfo
    }

    override fun toRecyclerItem(): RecyclerItem {
        return RecyclerItem(this, R.layout.item_rule_info, BR.itemVM)
    }

    fun onClickActivate() = onClickActivate(ruleInfo)
    fun onClickNotiOnTrigger() = onClickNotiOnTrigger(ruleInfo)
    fun onClickDelete() = onClickDelete(ruleInfo.ruleId, ruleInfo.ruleName)
    fun onClickItem() = onClickItem(ruleInfo.ruleId)
}
