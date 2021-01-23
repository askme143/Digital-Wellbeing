package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.*
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.AppBlockActionDto
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.DndActionDto
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.NotificationActionDto
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.action.RingerActionDto
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.rule.RuleDto
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.rule.RuleInfoDto
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger.ActivityTriggerDto
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger.LocationTriggerDto
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger.TimeTriggerDto
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
    private val emptyRule = RuleDto(
        RuleInfoDto(TEMPORAL_RID, "규칙 이름", activated = true, notiOnTrigger = false),
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

    fun init(ruleDto: RuleDto) {
        isNewRule = false
        editingRule.value = ruleDto.copy()

        initTriggerActionItemList()
    }

    fun init() {
        isNewRule = true
        editingRule.value = RuleDto(
            RuleInfoDto(TEMPORAL_RID, "규칙 이름", activated = true, notiOnTrigger = false),
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
            rule.locationTriggerDto?.let { addLocationTrigger(it) }
            rule.timeTriggerDto?.let { addTimeTrigger(it) }
            rule.activityTriggerDto?.let { addActivityTrigger(it) }

            rule.appBlockActionDto?.let { addAppBlockAction(it) }
            rule.notificationActionDto?.let { addNotificationAction(it) }
            rule.dndActionDto?.let { addDndAction(it) }
            rule.ringerActionDto?.let { addRingerAction(it) }
        }
    }

    fun addLocationTrigger(locationTriggerDto: LocationTriggerDto) {
        val newItem =
            TriggerActionItemViewModel(
                LOCATION_TRIGGER_TITLE,
                locationTriggerRuleItem(locationTriggerDto),
                onClickItem,
                onClickItemDelete
            ).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(locationTriggerDto = locationTriggerDto)
        _triggerItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is TriggerActionItemViewModel && vm.triggerActionItem.title == LOCATION_TRIGGER_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    fun addTimeTrigger(timeTriggerDto: TimeTriggerDto) {
        val newItem =
            TriggerActionItemViewModel(
                TIME_TRIGGER_TITLE,
                timeTriggerRuleItem(timeTriggerDto),
                onClickItem,
                onClickItemDelete
            ).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(timeTriggerDto = timeTriggerDto)
        _triggerItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is TriggerActionItemViewModel && vm.triggerActionItem.title == TIME_TRIGGER_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    fun addActivityTrigger(activityTriggerDto: ActivityTriggerDto) {
        val newItem =
            TriggerActionItemViewModel(
                ACTIVITY_TRIGGER_TITLE,
                activityTriggerRuleItem(activityTriggerDto),
                onClickItem,
                onClickItemDelete
            ).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(activityTriggerDto = activityTriggerDto)
        _triggerItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is TriggerActionItemViewModel && vm.triggerActionItem.title == ACTIVITY_TRIGGER_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    fun addAppBlockAction(appBlockActionDto: AppBlockActionDto) {
        val newItem =
            TriggerActionItemViewModel(
                APP_BLOCK_ACTION_TITLE,
                appBlockActionRuleItem(appBlockActionDto),
                onClickItem,
                onClickItemDelete
            ).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(appBlockActionDto = appBlockActionDto)
        _actionItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is TriggerActionItemViewModel && vm.triggerActionItem.title == APP_BLOCK_ACTION_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    fun addNotificationAction(notificationActionDto: NotificationActionDto) {
        val newItem =
            TriggerActionItemViewModel(
                NOTIFICATION_ACTION_TITLE,
                notificationActionRuleItem(notificationActionDto),
                onClickItem,
                onClickItemDelete
            ).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(notificationActionDto = notificationActionDto)
        _actionItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is TriggerActionItemViewModel && vm.triggerActionItem.title == NOTIFICATION_ACTION_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    fun addDndAction(dndActionDto: DndActionDto) {
        val newItem =
            TriggerActionItemViewModel(
                DND_ACTION_TITLE,
                dndActionRuleItem(dndActionDto),
                onClickItem,
                onClickItemDelete
            ).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(dndActionDto = dndActionDto)
        _actionItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is TriggerActionItemViewModel && vm.triggerActionItem.title == DND_ACTION_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    fun addRingerAction(ringerActionDto: RingerActionDto) {
        val newItem =
            TriggerActionItemViewModel(
                RINGER_ACTION_TITLE,
                ringerActionRuleItem(ringerActionDto),
                onClickItem,
                onClickItemDelete
            ).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(ringerActionDto = ringerActionDto)
        _actionItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is TriggerActionItemViewModel && vm.triggerActionItem.title == RINGER_ACTION_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    private fun locationTriggerRuleItem(locationTriggerDto: LocationTriggerDto): TriggerActionItem =
        object : TriggerActionItem {
            override val title = LOCATION_TRIGGER_TITLE
            override val description = locationTriggerDto.locationName
        }

    private fun timeTriggerRuleItem(timeTriggerDto: TimeTriggerDto): TriggerActionItem =
        object : TriggerActionItem {
            override val title = TIME_TRIGGER_TITLE
            override val description =
                "${
                    startEndMinutesToString(
                        timeTriggerDto.startTimeInMinutes,
                        timeTriggerDto.endTimeInMinutes
                    )
                }\n${TimeUtils.repeatDayToString(timeTriggerDto.repeatDay)}"
        }

    private fun activityTriggerRuleItem(activityTriggerDto: ActivityTriggerDto): TriggerActionItem =
        object : TriggerActionItem {
            override val title = ACTIVITY_TRIGGER_TITLE
            override val description = activityTriggerDto.activity
        }

    private fun appBlockActionRuleItem(appBlockActionDto: AppBlockActionDto): TriggerActionItem =
        object : TriggerActionItem {
            override val title = APP_BLOCK_ACTION_TITLE
            override val description = appBlockActionDto.appBlockEntryList.joinToString(", ") {
                "${it.appName} ${it.allowedTimeInMinutes}분 사용 시"
            }
        }

    private fun notificationActionRuleItem(notificationActionDto: NotificationActionDto): TriggerActionItem =
        object : TriggerActionItem {
            override val title = NOTIFICATION_ACTION_TITLE
            override val description = "${notificationActionDto.appList.joinToString("/")} 알림 숨김"
        }

    @Suppress("UNUSED_PARAMETER")
    private fun dndActionRuleItem(dndActionDto: DndActionDto): TriggerActionItem =
        object : TriggerActionItem {
            override val title = DND_ACTION_TITLE
            override val description = "방해 금지 모드 실행"
        }

    private fun ringerActionRuleItem(ringerActionDto: RingerActionDto): TriggerActionItem =
        object : TriggerActionItem {
            override val title = RINGER_ACTION_TITLE
            override val description: String = when (ringerActionDto.ringerMode) {
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

        when (id) {
            LOCATION_TRIGGER_TITLE ->
                editingRule.value = editingRule.value!!.copy(locationTriggerDto = null)
            TIME_TRIGGER_TITLE ->
                editingRule.value = editingRule.value!!.copy(timeTriggerDto = null)
            ACTIVITY_TRIGGER_TITLE ->
                editingRule.value = editingRule.value!!.copy(activityTriggerDto = null)

            APP_BLOCK_ACTION_TITLE ->
                editingRule.value = editingRule.value!!.copy(appBlockActionDto = null)
            NOTIFICATION_ACTION_TITLE ->
                editingRule.value = editingRule.value!!.copy(notificationActionDto = null)
            DND_ACTION_TITLE ->
                editingRule.value = editingRule.value!!.copy(dndActionDto = null)
            RINGER_ACTION_TITLE ->
                editingRule.value = editingRule.value!!.copy(ringerActionDto = null)
        }
    }
}