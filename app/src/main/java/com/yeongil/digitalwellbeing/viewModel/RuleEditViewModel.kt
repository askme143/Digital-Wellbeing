package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.*
import com.yeongil.digitalwellbeing.data.dto.action.AppBlockAction
import com.yeongil.digitalwellbeing.data.dto.action.DndAction
import com.yeongil.digitalwellbeing.data.dto.action.NotificationAction
import com.yeongil.digitalwellbeing.data.dto.action.RingerAction
import com.yeongil.digitalwellbeing.data.dto.rule.Rule
import com.yeongil.digitalwellbeing.data.dto.rule.RuleInfo
import com.yeongil.digitalwellbeing.data.dto.trigger.ActivityTrigger
import com.yeongil.digitalwellbeing.data.dto.trigger.LocationTrigger
import com.yeongil.digitalwellbeing.data.dto.trigger.TimeTrigger
import com.yeongil.digitalwellbeing.repository.RuleRepository
import com.yeongil.digitalwellbeing.utils.*
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.TriggerActionItem
import com.yeongil.digitalwellbeing.utils.TimeUtils.startEndMinutesToString
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItemViewModel
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.TriggerActionItemViewModel

class RuleEditViewModel(
    private val ruleRepository: RuleRepository
) : ViewModel() {
    private val emptyRule = Rule(
        RuleInfo(TEMPORAL_RID, "규칙 이름", activated = true, notiOnTrigger = false),
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

    private val _triggerItemList = MutableLiveData<List<RecyclerItem>>()
    val triggerItemList: LiveData<List<RecyclerItem>> get() = _triggerItemList

    private val _actionItemList = MutableLiveData<List<RecyclerItem>>()
    val actionItemList: LiveData<List<RecyclerItem>> get() = _actionItemList

    val itemClickEvent = MutableLiveData<Event<String>>()

    fun init(rule: Rule) {
        isNewRule = false
        editingRule.value = rule.copy()

        initTriggerActionItemList()
    }

    fun init() {
        isNewRule = true
        editingRule.value = Rule(
            RuleInfo(TEMPORAL_RID, "규칙 이름", activated = true, notiOnTrigger = false),
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )

        initTriggerActionItemList()
    }

    private fun initTriggerActionItemList() {
        _triggerItemList.value = listOf()
        _actionItemList.value = listOf()

        val rule = editingRule.value

        if (rule != null) {
            rule.locationTrigger?.let { addLocationTrigger(it) }
            rule.timeTrigger?.let { addTimeTrigger(it) }
            rule.activityTrigger?.let { addActivityTrigger(it) }

            rule.appBlockAction?.let { addAppBlockAction(it) }
            rule.notificationAction?.let { addNotificationAction(it) }
            rule.dndAction?.let { addDndAction(it) }
            rule.ringerAction?.let { addRingerAction(it) }
        }
    }

    fun addLocationTrigger(locationTrigger: LocationTrigger) {
        val newItem =
            TriggerActionItemViewModel(
                LOCATION_TRIGGER_TITLE,
                locationTriggerRuleItem(locationTrigger),
                onClickItem,
                onClickItemDelete
            ).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(locationTrigger = locationTrigger)
        _triggerItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is TriggerActionItemViewModel && vm.triggerActionItem.title == LOCATION_TRIGGER_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    fun addTimeTrigger(timeTrigger: TimeTrigger) {
        val newItem =
            TriggerActionItemViewModel(
                TIME_TRIGGER_TITLE,
                timeTriggerRuleItem(timeTrigger),
                onClickItem,
                onClickItemDelete
            ).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(timeTrigger = timeTrigger)
        _triggerItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is TriggerActionItemViewModel && vm.triggerActionItem.title == TIME_TRIGGER_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    fun addActivityTrigger(activityTrigger: ActivityTrigger) {
        val newItem =
            TriggerActionItemViewModel(
                ACTIVITY_TRIGGER_TITLE,
                activityTriggerRuleItem(activityTrigger),
                onClickItem,
                onClickItemDelete
            ).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(activityTrigger = activityTrigger)
        _triggerItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is TriggerActionItemViewModel && vm.triggerActionItem.title == ACTIVITY_TRIGGER_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    fun addAppBlockAction(appBlockAction: AppBlockAction) {
        val newItem =
            TriggerActionItemViewModel(
                APP_BLOCK_ACTION_TITLE,
                appBlockActionRuleItem(appBlockAction),
                onClickItem,
                onClickItemDelete
            ).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(appBlockAction = appBlockAction)
        _actionItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is TriggerActionItemViewModel && vm.triggerActionItem.title == APP_BLOCK_ACTION_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    fun addNotificationAction(notificationAction: NotificationAction) {
        val newItem =
            TriggerActionItemViewModel(
                NOTIFICATION_ACTION_TITLE,
                notificationActionRuleItem(notificationAction),
                onClickItem,
                onClickItemDelete
            ).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(notificationAction = notificationAction)
        _actionItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is TriggerActionItemViewModel && vm.triggerActionItem.title == NOTIFICATION_ACTION_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    fun addDndAction(dndAction: DndAction) {
        val newItem =
            TriggerActionItemViewModel(
                DND_ACTION_TITLE,
                dndActionRuleItem(dndAction),
                onClickItem,
                onClickItemDelete
            ).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(dndAction = dndAction)
        _actionItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is TriggerActionItemViewModel && vm.triggerActionItem.title == DND_ACTION_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    fun addRingerAction(ringerAction: RingerAction) {
        val newItem =
            TriggerActionItemViewModel(
                RINGER_ACTION_TITLE,
                ringerActionRuleItem(ringerAction),
                onClickItem,
                onClickItemDelete
            ).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(ringerAction = ringerAction)
        _actionItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is TriggerActionItemViewModel && vm.triggerActionItem.title == RINGER_ACTION_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    private fun locationTriggerRuleItem(locationTrigger: LocationTrigger): TriggerActionItem =
        object : TriggerActionItem {
            override val title = LOCATION_TRIGGER_TITLE
            override val description = locationTrigger.locationName
        }

    private fun timeTriggerRuleItem(timeTrigger: TimeTrigger): TriggerActionItem =
        object : TriggerActionItem {
            override val title = TIME_TRIGGER_TITLE
            override val description =
                "${
                    startEndMinutesToString(
                        timeTrigger.startTimeInMinutes,
                        timeTrigger.endTimeInMinutes
                    )
                }\n${TimeUtils.repeatDayToString(timeTrigger.repeatDay)}"
        }

    private fun activityTriggerRuleItem(activityTrigger: ActivityTrigger): TriggerActionItem =
        object : TriggerActionItem {
            override val title = ACTIVITY_TRIGGER_TITLE
            override val description = activityTrigger.activity
        }

    private fun appBlockActionRuleItem(appBlockAction: AppBlockAction): TriggerActionItem =
        object : TriggerActionItem {
            override val title = APP_BLOCK_ACTION_TITLE
            override val description = appBlockAction.AppBlockEntries.joinToString(", ") {
                "${it.appName} ${it.allowedTimeInMinutes}분 사용 시"
            }
        }

    private fun notificationActionRuleItem(notificationAction: NotificationAction): TriggerActionItem =
        object : TriggerActionItem {
            override val title = NOTIFICATION_ACTION_TITLE
            override val description = "${notificationAction.appList.joinToString("/")} 알림 숨김"
        }

    @Suppress("UNUSED_PARAMETER")
    private fun dndActionRuleItem(dndAction: DndAction): TriggerActionItem =
        object : TriggerActionItem {
            override val title = DND_ACTION_TITLE
            override val description = "방해 금지 모드 실행"
        }

    private fun ringerActionRuleItem(ringerAction: RingerAction): TriggerActionItem =
        object : TriggerActionItem {
            override val title = RINGER_ACTION_TITLE
            override val description: String = when (ringerAction.ringerMode) {
                VIBRATE -> "진동 모드로 변경"
                RING -> "소리 모드로 변경"
                SILENT -> "무음 모드로 변경"
                else -> "ERROR"
            }
        }

    private val onClickItem: (String) -> Unit = { id ->
        itemClickEvent.value = Event(id)
    }

    private val onClickItemDelete: (String) -> Unit = { id ->
        _triggerItemList.value =
            triggerItemList.value?.filterNot {
                (it.viewModel is RecyclerItemViewModel && it.viewModel.id == id)
            }
        _actionItemList.value =
            actionItemList.value?.filterNot {
                (it.viewModel is RecyclerItemViewModel && it.viewModel.id == id)
            }
    }
}