package com.yeongil.digitalwellbeing.viewModel.itemViewModel

import com.yeongil.digitalwellbeing.BR
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.data.rule.RuleInfo
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dao.rule.RuleDao
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.dto.rule.RuleInfoDto
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItemViewModel
import kotlinx.coroutines.CoroutineScope

// ID is RuleID
class RuleInfoItemViewModel(
    override val id: String,
    val ruleInfo: RuleInfo,
    private val onClickActivate: (RuleInfo) -> Unit,
    private val onClickNotiOnTrigger: (RuleInfo) -> Unit,
    private val onClickDelete: (Int) -> Unit,
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
    fun onClickDelete() = onClickDelete(ruleInfo.ruleId)
    fun onClickItem() = onClickItem(ruleInfo.ruleId)
}
