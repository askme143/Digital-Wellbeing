package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.*
import com.yeongil.digitalwellbeing.R
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
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.RuleMemberItem
import com.yeongil.digitalwellbeing.utils.TimeUtils.startEndMinutesToString
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.RuleMemberItemViewModel

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
    val editingRule = MutableLiveData<Rule>(emptyRule)

    private val _triggerItemList = MutableLiveData<List<RecyclerItem>>()
    val triggerItemList: LiveData<List<RecyclerItem>> get() = _triggerItemList

    private val _actionItemList = MutableLiveData<List<RecyclerItem>>()
    val actionItemList: LiveData<List<RecyclerItem>> get() = _actionItemList

    val itemClickEvent = MutableLiveData<Event<String>>()

    init {
        init()
        test()
    }

    fun init(rule: Rule) {
        isNewRule = false
        editingRule.value = rule.copy()

        initRuleMemberItemList()
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

        initRuleMemberItemList()
    }

    private fun initRuleMemberItemList() {
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

    private fun test() {
//        val sampleLocationTrigger = LocationTrigger(1, 1.0, 1.0, 1, "sample")
//        val sampleTimeTrigger =
//            TimeTrigger(1, 1, 1, listOf(false, false, false, false, false, false, true))
//        val sampleActivityTrigger = ActivityTrigger(1, "Driving")
//
//        _triggerItemList.value =
//            listOf(RuleMemberItemViewModel(sampleLocationTrigger, _triggerItemList).toRecyclerItem())
    }

    fun addLocationTrigger(locationTrigger: LocationTrigger) {
        val newItem =
            RuleMemberItemViewModel(locationTriggerRuleItem(locationTrigger)).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(locationTrigger = locationTrigger)
        _triggerItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is RuleMemberItemViewModel && vm.ruleMemberItem.title == LOCATION_TRIGGER_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    fun addTimeTrigger(timeTrigger: TimeTrigger) {
        val newItem =
            RuleMemberItemViewModel(timeTriggerRuleItem(timeTrigger)).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(timeTrigger = timeTrigger)
        _triggerItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is RuleMemberItemViewModel && vm.ruleMemberItem.title == TIME_TRIGGER_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    fun addActivityTrigger(activityTrigger: ActivityTrigger) {
        val newItem =
            RuleMemberItemViewModel(activityTriggerRuleItem(activityTrigger)).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(activityTrigger = activityTrigger)
        _triggerItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is RuleMemberItemViewModel && vm.ruleMemberItem.title == ACTIVITY_TRIGGER_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    fun addAppBlockAction(appBlockAction: AppBlockAction) {
        val newItem =
            RuleMemberItemViewModel(appBlockActionRuleItem(appBlockAction)).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(appBlockAction = appBlockAction)
        _actionItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is RuleMemberItemViewModel && vm.ruleMemberItem.title == APP_BLOCK_ACTION_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    fun addNotificationAction(notificationAction: NotificationAction) {
        val newItem =
            RuleMemberItemViewModel(notificationActionRuleItem(notificationAction)).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(notificationAction = notificationAction)
        _actionItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is RuleMemberItemViewModel && vm.ruleMemberItem.title == NOTIFICATION_ACTION_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    fun addDndAction(dndAction: DndAction) {
        val newItem =
            RuleMemberItemViewModel(dndActionRuleItem(dndAction)).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(dndAction = dndAction)
        _actionItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is RuleMemberItemViewModel && vm.ruleMemberItem.title == DND_ACTION_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    fun addRingerAction(ringerAction: RingerAction) {
        val newItem =
            RuleMemberItemViewModel(ringerActionRuleItem(ringerAction)).toRecyclerItem()

        editingRule.value = editingRule.value!!.copy(ringerAction = ringerAction)
        _actionItemList.value = _triggerItemList.value?.filterNot {
            val vm = it.viewModel
            vm is RuleMemberItemViewModel && vm.ruleMemberItem.title == RINGER_ACTION_TITLE
        }?.plus(newItem) ?: listOf(newItem)
    }

    private fun locationTriggerRuleItem(locationTrigger: LocationTrigger): RuleMemberItem =
        object : RuleMemberItem {
            override val title = LOCATION_TRIGGER_TITLE
            override val description = locationTrigger.locationName
            override val layoutId = R.layout.item_trigger

            override fun delete(recyclerItem: RecyclerItem) {
                _triggerItemList.value = triggerItemList.value?.minus(recyclerItem)
                editingRule.value = editingRule.value?.copy(locationTrigger = null)
            }

            override fun click() {
                itemClickEvent.value = Event(title)
            }
        }

    private fun timeTriggerRuleItem(timeTrigger: TimeTrigger): RuleMemberItem =
        object : RuleMemberItem {
            override val title = TIME_TRIGGER_TITLE
            override val description =
                "${
                    startEndMinutesToString(
                        timeTrigger.startTimeInMinutes,
                        timeTrigger.endTimeInMinutes
                    )
                }\n${TimeUtils.repeatDayToString(timeTrigger.repeatDay)}"
            override val layoutId = R.layout.item_trigger

            override fun delete(recyclerItem: RecyclerItem) {
                _triggerItemList.value = triggerItemList.value?.minus(recyclerItem)
                editingRule.value = editingRule.value?.copy(timeTrigger = null)
            }

            override fun click() {
                itemClickEvent.value = Event(title)
            }
        }

    private fun activityTriggerRuleItem(activityTrigger: ActivityTrigger): RuleMemberItem =
        object : RuleMemberItem {
            override val title = ACTIVITY_TRIGGER_TITLE
            override val description = activityTrigger.activity
            override val layoutId = R.layout.item_trigger

            override fun delete(recyclerItem: RecyclerItem) {
                _triggerItemList.value = triggerItemList.value?.minus(recyclerItem)
                editingRule.value = editingRule.value?.copy(activityTrigger = null)
            }

            override fun click() {
                itemClickEvent.value = Event(title)
            }
        }

    private fun appBlockActionRuleItem(appBlockAction: AppBlockAction): RuleMemberItem =
        object : RuleMemberItem {
            override val title = APP_BLOCK_ACTION_TITLE
            override val description = appBlockAction.AppBlockEntries.joinToString(", ") {
                "${it.appName} ${it.allowedTimeInMinutes}분 사용 시"
            }
            override val layoutId = R.layout.item_action

            override fun delete(recyclerItem: RecyclerItem) {
                _actionItemList.value = triggerItemList.value?.minus(recyclerItem)
                editingRule.value = editingRule.value?.copy(appBlockAction = null)
            }

            override fun click() {
                itemClickEvent.value = Event(title)
            }
        }

    private fun notificationActionRuleItem(notificationAction: NotificationAction): RuleMemberItem =
        object : RuleMemberItem {
            override val title = NOTIFICATION_ACTION_TITLE
            override val description = "${notificationAction.appList.joinToString("/")} 알림 숨김"
            override val layoutId = R.layout.item_action

            override fun delete(recyclerItem: RecyclerItem) {
                _actionItemList.value = triggerItemList.value?.minus(recyclerItem)
                editingRule.value = editingRule.value?.copy(notificationAction = null)
            }

            override fun click() {
                itemClickEvent.value = Event(title)
            }
        }

    private fun dndActionRuleItem(dndAction: DndAction): RuleMemberItem =
        object : RuleMemberItem {
            override val title = DND_ACTION_TITLE
            override val description = "방해 금지 모드 실행"
            override val layoutId = R.layout.item_action

            override fun delete(recyclerItem: RecyclerItem) {
                _actionItemList.value = triggerItemList.value?.minus(recyclerItem)
                editingRule.value = editingRule.value?.copy(dndAction = null)
            }

            override fun click() {
                itemClickEvent.value = Event(title)
            }
        }

    private fun ringerActionRuleItem(ringerAction: RingerAction): RuleMemberItem =
        object : RuleMemberItem {
            override val title = RINGER_ACTION_TITLE
            override val description: String = when (ringerAction.ringerMode) {
                VIBRATE -> "진동 모드로 변경"
                RING -> "소리 모드로 변경"
                SILENT -> "무음 모드로 변경"
                else -> "ERROR"
            }
            override val layoutId = R.layout.item_action

            override fun delete(recyclerItem: RecyclerItem) {
                _actionItemList.value = triggerItemList.value?.minus(recyclerItem)
                editingRule.value = editingRule.value?.copy(ringerAction = null)
            }

            override fun click() {
                itemClickEvent.value = Event(title)
            }
        }
}