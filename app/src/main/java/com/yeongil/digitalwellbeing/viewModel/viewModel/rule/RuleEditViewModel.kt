package com.yeongil.digitalwellbeing.viewModel.viewModel.rule

import android.util.Log
import androidx.lifecycle.*
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.data.action.AppBlockAction
import com.yeongil.digitalwellbeing.data.action.DndAction
import com.yeongil.digitalwellbeing.data.action.NotificationAction
import com.yeongil.digitalwellbeing.data.action.RingerAction
import com.yeongil.digitalwellbeing.data.rule.Rule
import com.yeongil.digitalwellbeing.data.rule.RuleInfo
import com.yeongil.digitalwellbeing.data.trigger.ActivityTrigger
import com.yeongil.digitalwellbeing.data.trigger.LocationTrigger
import com.yeongil.digitalwellbeing.data.trigger.TimeTrigger
import com.yeongil.digitalwellbeing.repository.PackageManagerRepository
import com.yeongil.digitalwellbeing.repository.RuleRepository
import com.yeongil.digitalwellbeing.utils.*
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.viewModel.item.*
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.TriggerActionItemViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RuleEditViewModel(
    private val ruleRepo: RuleRepository,
    private val pmRepo: PackageManagerRepository
) : ViewModel() {
    private val emptyRule = Rule(
        RuleInfo(TEMPORAL_RULE_ID, "규칙 이름", activated = true, notiOnTrigger = false),
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
    var isNewRule: Boolean = true
    val editingRule = MutableLiveData(emptyRule)

    private val triggerActionItemList = MutableLiveData<List<TriggerActionItem>>()
    val triggerRecyclerItemList = liveData<List<RecyclerItem>> {
        triggerActionItemList.asFlow().collect { list ->
            emit(
                list.filter { it.isTrigger }
                    .map {
                        TriggerActionItemViewModel(
                            it.title,
                            R.layout.item_trigger_action,
                            it,
                            onClickItem,
                            onClickItemDelete
                        )
                    }
                    .map { it.toRecyclerItem() }
            )
        }
    }
    val actionRecyclerItemList = liveData<List<RecyclerItem>> {
        triggerActionItemList.asFlow().collect { list ->
            emit(
                list.filter { it.isAction }
                    .map {
                        TriggerActionItemViewModel(
                            it.title,
                            R.layout.item_trigger_action,
                            it,
                            onClickItem,
                            onClickItemDelete
                        )
                    }
                    .map { it.toRecyclerItem() }
            )
        }
    }
    val itemClickEvent = MutableLiveData<Event<String>>()
    val ruleName = MutableLiveData<String>()

    fun init(rule: Rule) {
        isNewRule = false
        editingRule.value = rule.copy()
        ruleName.value = editingRule.value?.ruleInfo?.ruleName ?: "규칙 이름"

        initTriggerActionItemList()
    }

    fun init() {
        isNewRule = true
        editingRule.value = emptyRule.copy()
        ruleName.value = editingRule.value?.ruleInfo?.ruleName ?: "규칙 이름"

        initTriggerActionItemList()
    }

    private fun initTriggerActionItemList() {
        triggerActionItemList.value = listOf()

        val rule = editingRule.value

        if (rule != null) {
            rule.locationTrigger?.let { addTriggerAction(it) }
            rule.timeTrigger?.let { addTriggerAction(it) }
            rule.activityTrigger?.let { addTriggerAction(it) }

            rule.appBlockAction?.let { addTriggerAction(it) }
            rule.notificationAction?.let { addTriggerAction(it) }
            rule.dndAction?.let { addTriggerAction(it) }
            rule.ringerAction?.let { addTriggerAction(it) }
        }
    }

    fun saveRule() {
        val rule = editingRule.value ?: return
        val ruleInfo = rule.ruleInfo.copy(ruleName = ruleName.value ?: "규칙 이름")
        val savingRule = rule.copy(ruleInfo = ruleInfo)
        editingRule.value = savingRule

        viewModelScope.launch(Dispatchers.IO) {
            ruleRepo.insertOrUpdateRule(savingRule)
        }
    }

    fun addTriggerAction(triggerAction: Any) {
        val item = when (triggerAction) {
            is LocationTrigger -> {
                editingRule.value = editingRule.value!!.copy(locationTrigger = triggerAction)
                TriggerActionItem(triggerAction)
            }
            is TimeTrigger -> {
                editingRule.value = editingRule.value!!.copy(timeTrigger = triggerAction)
                TriggerActionItem(triggerAction)
            }
            is ActivityTrigger -> {
                editingRule.value = editingRule.value!!.copy(activityTrigger = triggerAction)
                TriggerActionItem(triggerAction)
            }
            is AppBlockAction -> {
                editingRule.value = editingRule.value!!.copy(appBlockAction = triggerAction)
                TriggerActionItem(triggerAction, pmRepo)
            }
            is NotificationAction -> {
                editingRule.value = editingRule.value!!.copy(notificationAction = triggerAction)
                TriggerActionItem(triggerAction, pmRepo)
            }
            is DndAction -> {
                editingRule.value = editingRule.value!!.copy(dndAction = triggerAction)
                TriggerActionItem(triggerAction)
            }
            is RingerAction -> {
                editingRule.value = editingRule.value!!.copy(ringerAction = triggerAction)
                TriggerActionItem(triggerAction)
            }
            else -> return
        }

        val oldList = triggerActionItemList.value ?: listOf()
        val index = oldList.map { it.title }.indexOf(item.title)

        if (index == -1) {
            triggerActionItemList.value = oldList + item
        } else {
            triggerActionItemList.value =
                oldList.subList(0, index) + item + oldList.subList(index + 1, oldList.size)
        }
    }

    private val onClickItem: (String) -> Unit = { id ->
        itemClickEvent.value = Event(id)
    }

    private val onClickItemDelete: (String) -> Unit = { id ->
        when (id) {
            LOCATION_TRIGGER_TITLE -> editingRule.value =
                editingRule.value!!.copy(locationTrigger = null)
            TIME_TRIGGER_TITLE -> editingRule.value =
                editingRule.value!!.copy(timeTrigger = null)
            ACTIVITY_TRIGGER_TITLE -> editingRule.value =
                editingRule.value!!.copy(activityTrigger = null)
            APP_BLOCK_ACTION_TITLE -> editingRule.value =
                editingRule.value!!.copy(appBlockAction = null)
            NOTIFICATION_ACTION_TITLE -> editingRule.value =
                editingRule.value!!.copy(notificationAction = null)
            DND_ACTION_TITLE ->
                editingRule.value = editingRule.value!!.copy(dndAction = null)
            RINGER_ACTION_TITLE -> editingRule.value =
                editingRule.value!!.copy(ringerAction = null)
        }

        triggerActionItemList.value = triggerActionItemList.value?.filterNot { it.title == id }
    }
}