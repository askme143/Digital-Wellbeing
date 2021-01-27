package com.yeongil.digitalwellbeing.viewModel.viewModel.rule

import android.util.Log
import androidx.lifecycle.*
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.data.rule.Rule
import com.yeongil.digitalwellbeing.repository.PackageManagerRepository
import com.yeongil.digitalwellbeing.repository.RuleRepository
import com.yeongil.digitalwellbeing.utils.TEMPORAL_RULE_ID
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.viewModel.item.TriggerActionItem
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.TriggerActionItemViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DescriptionViewModel(
    private val ruleRepo: RuleRepository,
    private val pmRepo: PackageManagerRepository
) : ViewModel() {
    private var rid = TEMPORAL_RULE_ID
    private val currentRule = MutableLiveData(Rule())

    val ruleName = liveData<String> {
        currentRule.asFlow().collect { emit(it.ruleInfo.ruleName) }
    }

    val triggerRecyclerItemList = liveData<List<RecyclerItem>> {
        currentRule.asFlow().collect { rule ->
            val triggerActionItemList = ArrayList<TriggerActionItem>()

            rule.locationTrigger?.let { triggerActionItemList.add(TriggerActionItem(it)) }
            rule.timeTrigger?.let { triggerActionItemList.add(TriggerActionItem(it)) }
            rule.activityTrigger?.let { triggerActionItemList.add(TriggerActionItem(it)) }

            val recyclerItemList = triggerActionItemList
                .map {
                    TriggerActionItemViewModel(
                        it.title, R.layout.item_trigger_action_description, it, {}, {})
                }.map { it.toRecyclerItem() }
            emit(recyclerItemList)
        }
    }
    val actionRecyclerItemList = liveData<List<RecyclerItem>> {
        currentRule.asFlow().collect { rule ->
            val triggerActionItemList = ArrayList<TriggerActionItem>()

            rule.appBlockAction?.let { triggerActionItemList.add(TriggerActionItem(it, pmRepo)) }
            rule.notificationAction?.let {
                triggerActionItemList.add(TriggerActionItem(it, pmRepo))
            }
            rule.dndAction?.let { triggerActionItemList.add(TriggerActionItem(it)) }
            rule.ringerAction?.let { triggerActionItemList.add(TriggerActionItem(it)) }

            val recyclerItemList = triggerActionItemList
                .map {
                    TriggerActionItemViewModel(
                        it.title, R.layout.item_trigger_action_description, it, {}, {})
                }.map { it.toRecyclerItem() }
            emit(recyclerItemList)
        }
    }

    val editingRuleName = MutableLiveData<String>()
    fun ruleNameSubmit() = run {
        val ruleName = editingRuleName.value!!
        val currentRuleInfo = currentRule.value!!.ruleInfo
        currentRule.value =
            currentRule.value!!.copy(ruleInfo = currentRuleInfo.copy(ruleName = ruleName))

        viewModelScope.launch(Dispatchers.IO) {
            ruleRepo.updateRuleInfo(currentRuleInfo.copy(ruleName = ruleName))
        }
    }

    fun deleteRule() = run {
        viewModelScope.launch(Dispatchers.IO) {
            ruleRepo.deleteRuleByRid(rid)
        }
    }

    fun init(rid: Int) = run {
        this.rid = rid
        currentRule.value = Rule()
        viewModelScope.launch {
            val rule = ruleRepo.getRuleByRid(rid)
            launch(Dispatchers.Main) { currentRule.value = rule }
        }
    }

    fun init(rule: Rule) = run {
        this.rid = rule.ruleInfo.ruleId
        currentRule.value = rule
    }

    fun initEditingRuleName() = run { editingRuleName.value = ruleName.value }

    fun getRule() = currentRule.value!!
}