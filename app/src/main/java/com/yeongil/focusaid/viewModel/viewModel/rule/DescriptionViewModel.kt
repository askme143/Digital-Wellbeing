package com.yeongil.focusaid.viewModel.viewModel.rule

import androidx.lifecycle.*
import com.yeongil.focusaid.R
import com.yeongil.focusaid.data.rule.Rule
import com.yeongil.focusaid.repository.PackageManagerRepository
import com.yeongil.focusaid.repository.RuleRepository
import com.yeongil.focusaid.utils.Event
import com.yeongil.focusaid.utils.TEMPORAL_RULE_ID
import com.yeongil.focusaid.viewModel.item.TriggerActionItem
import com.yeongil.focusaid.viewModel.itemViewModel.TriggerActionItemViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DescriptionViewModel(
    private val ruleRepo: RuleRepository,
    private val pmRepo: PackageManagerRepository
) : ViewModel() {
    private var rid = TEMPORAL_RULE_ID
    private val _rule = MutableLiveData(Rule())

    // View Related Live Data
    val rule: LiveData<Rule> get() = _rule
    val ruleName = _rule.map { it.ruleInfo.ruleName }

    val itemClickEvent = MutableLiveData<Event<String>>()
    val onItemClick: (String) -> Unit = { id ->
        itemClickEvent.value = Event(id)
    }

    val triggerRecyclerItemList = _rule.map { rule ->
        val triggerActionItemList = listOfNotNull(
            rule.locationTrigger?.let { TriggerActionItem(it) },
            rule.timeTrigger?.let { TriggerActionItem(it) },
            rule.activityTrigger?.let { TriggerActionItem(it) },
        )

        triggerActionItemList
            .map {
                TriggerActionItemViewModel(
                    it.title, R.layout.item_trigger_action_description, it, onItemClick, {})
                    .toRecyclerItem()
            }
    }

    val actionRecyclerItemList = _rule.map { rule ->
        val triggerActionItemList = listOfNotNull(
            rule.appBlockAction?.let { TriggerActionItem(it, pmRepo) },
            rule.notificationAction?.let { TriggerActionItem(it, pmRepo) },
            rule.dndAction?.let { TriggerActionItem(it) },
            rule.ringerAction?.let { TriggerActionItem(it) },
        )

        triggerActionItemList
            .map {
                TriggerActionItemViewModel(
                    it.title, R.layout.item_trigger_action_description, it, onItemClick, {})
                    .toRecyclerItem()
            }
    }

    val editingRuleName = MutableLiveData<String>()
    fun updateRuleName() {
        val rule = _rule.value!!
        val ruleName = editingRuleName.value!!
        _rule.value = rule.copy(ruleInfo = rule.ruleInfo.copy(ruleName = ruleName))

        viewModelScope.launch(Dispatchers.IO) {
            ruleRepo.updateRuleInfo(rule.ruleInfo.copy(ruleName = ruleName))
        }
    }

    fun putRule(rid: Int) {
        this.rid = rid
        _rule.value = Rule()
        viewModelScope.launch {
            val rule = ruleRepo.getRuleByRid(rid)
            launch(Dispatchers.Main) { _rule.value = rule }
        }
    }

    fun putRule(rule: Rule) {
        this.rid = rule.ruleInfo.ruleId
        _rule.value = rule
    }

    fun refreshEditingRuleName() {
        editingRuleName.value = ruleName.value
    }
}