package com.yeongil.digitalwellbeing.viewModel.viewModel.rule

import androidx.lifecycle.*
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.data.rule.action.AppBlockAction
import com.yeongil.digitalwellbeing.data.rule.action.DndAction
import com.yeongil.digitalwellbeing.data.rule.action.NotificationAction
import com.yeongil.digitalwellbeing.data.rule.action.RingerAction
import com.yeongil.digitalwellbeing.data.rule.Rule
import com.yeongil.digitalwellbeing.data.rule.RuleInfo
import com.yeongil.digitalwellbeing.data.rule.trigger.ActivityTrigger
import com.yeongil.digitalwellbeing.data.rule.trigger.LocationTrigger
import com.yeongil.digitalwellbeing.data.rule.trigger.TimeTrigger
import com.yeongil.digitalwellbeing.repository.PackageManagerRepository
import com.yeongil.digitalwellbeing.repository.RuleRepository
import com.yeongil.digitalwellbeing.utils.*
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.viewModel.item.*
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.TriggerActionItemViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
    var originalRule: Rule = emptyRule
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

    val itemAddEvent = MutableLiveData<Event<Unit>>()
    val itemEditEvent = MutableLiveData<Event<Unit>>()

    val itemClickEvent = MutableLiveData<Event<String>>()

    val itemDeleteEvent = MutableLiveData<Event<Boolean>>()
    var deletingItemId = ""

    val isActionItemEmpty = liveData<Boolean> {
        actionRecyclerItemList.asFlow().collect { emit(it.isEmpty()) }
    }

    val ruleName = MutableLiveData<String>()

    fun init(rule: Rule) {
        isNewRule = false
        originalRule = rule.copy()
        editingRule.value = rule.copy()
        ruleName.value = editingRule.value?.ruleInfo?.ruleName ?: "규칙 이름"

        initTriggerActionItemList()
    }

    fun init() {
        isNewRule = true
        originalRule = emptyRule
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

        itemAddEvent.value?.getContentIfNotHandled()
        itemEditEvent.value?.getContentIfNotHandled()
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
        val (item, isNewItem, isEdited) = when (triggerAction) {
            is LocationTrigger -> {
                val oldTriggerAction = editingRule.value!!.locationTrigger
                val isNewItem = oldTriggerAction == null
                val isEdited = oldTriggerAction != triggerAction

                editingRule.value = editingRule.value!!.copy(locationTrigger = triggerAction)
                Triple(TriggerActionItem(triggerAction), isNewItem, isEdited)
            }
            is TimeTrigger -> {
                val oldTriggerAction = editingRule.value!!.timeTrigger
                val isNewItem = oldTriggerAction == null
                val isEdited = oldTriggerAction != triggerAction

                editingRule.value = editingRule.value!!.copy(timeTrigger = triggerAction)
                Triple(TriggerActionItem(triggerAction), isNewItem, isEdited)
            }
            is ActivityTrigger -> {
                val oldTriggerAction = editingRule.value!!.activityTrigger
                val isNewItem = oldTriggerAction == null
                val isEdited = oldTriggerAction != triggerAction

                editingRule.value = editingRule.value!!.copy(activityTrigger = triggerAction)
                Triple(TriggerActionItem(triggerAction), isNewItem, isEdited)
            }
            is AppBlockAction -> {
                val oldTriggerAction = editingRule.value!!.appBlockAction
                val isNewItem = oldTriggerAction == null
                val isEdited = oldTriggerAction != triggerAction

                editingRule.value = editingRule.value!!.copy(appBlockAction = triggerAction)
                Triple(TriggerActionItem(triggerAction, pmRepo), isNewItem, isEdited)
            }
            is NotificationAction -> {
                val oldTriggerAction = editingRule.value!!.notificationAction
                val isNewItem = oldTriggerAction == null
                val isEdited = oldTriggerAction != triggerAction

                editingRule.value = editingRule.value!!.copy(notificationAction = triggerAction)
                Triple(TriggerActionItem(triggerAction, pmRepo), isNewItem, isEdited)
            }
            is DndAction -> {
                val oldTriggerAction = editingRule.value!!.dndAction
                val isNewItem = oldTriggerAction == null
                val isEdited = oldTriggerAction != triggerAction

                editingRule.value = editingRule.value!!.copy(dndAction = triggerAction)
                Triple(TriggerActionItem(triggerAction), isNewItem, isEdited)
            }
            is RingerAction -> {
                val oldTriggerAction = editingRule.value!!.ringerAction
                val isNewItem = oldTriggerAction == null
                val isEdited = oldTriggerAction != triggerAction

                editingRule.value = editingRule.value!!.copy(ringerAction = triggerAction)
                Triple(TriggerActionItem(triggerAction), isNewItem, isEdited)
            }
            else -> return
        }

        val oldList = triggerActionItemList.value ?: listOf()
        val index = oldList.map { it.title }.indexOf(item.title)

        if (index == -1) {
            triggerActionItemList.value = oldList + item
        } else {
            triggerActionItemList.value = oldList.toMutableList().apply { this[index] = item }
        }

        if (isNewItem) itemAddEvent.value = Event(Unit)
        else if (isEdited) itemEditEvent.value = Event(Unit)
    }

    fun deleteItem() {
        when (deletingItemId) {
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

        triggerActionItemList.value =
            triggerActionItemList.value?.filterNot { it.title == deletingItemId }
    }

    private val onClickItem: (String) -> Unit = { id ->
        itemClickEvent.value = Event(id)
    }

    private val onClickItemDelete: (String) -> Unit = { id ->
        deletingItemId = id
        itemDeleteEvent.value = Event(true)
    }
}