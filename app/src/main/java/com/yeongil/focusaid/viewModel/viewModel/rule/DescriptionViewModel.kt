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
import kotlinx.coroutines.withContext

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
    private val onItemClick: (String) -> Unit = { id ->
        itemClickEvent.value = Event(id)
    }

    val nameUpdateEvent = MutableLiveData<Event<Unit>>()
    val errorText = MutableLiveData<String>()

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
        val oldRule = _rule.value!!
        val ruleName = editingRuleName.value!!
        if (ruleName == "")
            errorText.value = "규칙 이름을 설정하세요."
        else {
            _rule.value = oldRule.copy(ruleInfo = oldRule.ruleInfo.copy(ruleName = ruleName))
            viewModelScope.launch(Dispatchers.IO) {
                val success = ruleRepo.updateRuleInfo(oldRule.ruleInfo.copy(ruleName = ruleName))
                withContext(Dispatchers.Main) {
                    if (success) {
                        errorText.value = ""
                        nameUpdateEvent.value = Event(Unit)
                    } else {
                        errorText.value = "이미 존재하는 이름입니다."
                        _rule.value = oldRule
                    }
                }
            }
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
        errorText.value = ""
    }
}