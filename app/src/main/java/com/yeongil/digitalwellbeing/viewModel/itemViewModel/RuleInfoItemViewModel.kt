package com.yeongil.digitalwellbeing.viewModel.itemViewModel

import com.yeongil.digitalwellbeing.BR
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.data.dao.rule.RuleDao
import com.yeongil.digitalwellbeing.data.dto.rule.RuleInfo
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItemViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RuleInfoItemViewModel(
    val ruleInfo: RuleInfo,
    private val ruleDao: RuleDao,
    private val coroutineScope: CoroutineScope
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

    fun onClickActivated() {
        val newRuleInfo =
            RuleInfo(ruleInfo.rid, ruleInfo.ruleName, !ruleInfo.activated, ruleInfo.notiOnTrigger)

        coroutineScope.launch { ruleDao.updateRuleInfo(newRuleInfo) }
    }

    fun onClickNotiOnTrigger() {
        val newRuleInfo =
            RuleInfo(ruleInfo.rid, ruleInfo.ruleName, ruleInfo.activated, !ruleInfo.notiOnTrigger)

        coroutineScope.launch { ruleDao.updateRuleInfo(newRuleInfo) }
    }

    fun onClickDelete() {
        coroutineScope.launch { ruleDao.deleteRuleByRid(ruleInfo.rid) }
    }

    fun onClick() {
        TODO("Navigate to a description fragment")
    }
}
