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
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.RuleMemberItem
import com.yeongil.digitalwellbeing.utils.TEMPORAL_RID
import com.yeongil.digitalwellbeing.utils.TimeUtils
import com.yeongil.digitalwellbeing.utils.TimeUtils.minutesToString
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

    init {
        initialize()
        test()
    }

    fun initialize(rule: Rule) {
        editingRule.value = rule.copy()
    }

    fun initialize() {
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
        editingRule.value = editingRule.value?.copy(locationTrigger = locationTrigger)

        val item =
            RuleMemberItemViewModel(locationTriggerRuleItem(locationTrigger)).toRecyclerItem()
        _triggerItemList.value =
            _triggerItemList.value?.plus(item) ?: listOf(item)
    }

    fun addTimeTrigger(timeTrigger: TimeTrigger) {
        editingRule.value = editingRule.value?.copy(timeTrigger = timeTrigger)

        val item =
            RuleMemberItemViewModel(timeTriggerRuleItem(timeTrigger)).toRecyclerItem()
        _triggerItemList.value =
            _triggerItemList.value?.plus(item) ?: listOf(item)
    }

    fun addActivityTrigger(activityTrigger: ActivityTrigger) {
        editingRule.value = editingRule.value?.copy(activityTrigger = activityTrigger)

        val item =
            RuleMemberItemViewModel(activityTriggerRuleItem(activityTrigger)).toRecyclerItem()
        _triggerItemList.value =
            _triggerItemList.value?.plus(item) ?: listOf(item)
    }

    fun addAppBlockAction(appBlockAction: AppBlockAction) {
        editingRule.value = editingRule.value?.copy(appBlockAction = appBlockAction)

        val item =
            RuleMemberItemViewModel(appBlockActionRuleItem(appBlockAction)).toRecyclerItem()
        _actionItemList.value =
            _actionItemList.value?.plus(item) ?: listOf(item)
    }

    fun addNotificationAction(notificationAction: NotificationAction) {
        editingRule.value = editingRule.value?.copy(notificationAction = notificationAction)

        val item =
            RuleMemberItemViewModel(notificationActionRuleItem(notificationAction)).toRecyclerItem()
        _actionItemList.value =
            _actionItemList.value?.plus(item) ?: listOf(item)
    }

    fun addDndAction(dndAction: DndAction) {
        editingRule.value = editingRule.value?.copy(dndAction = dndAction)

        val item =
            RuleMemberItemViewModel(dndActionRuleItem(dndAction)).toRecyclerItem()
        _actionItemList.value =
            _actionItemList.value?.plus(item) ?: listOf(item)
    }

    fun addRingerAction(ringerAction: RingerAction) {
        editingRule.value = editingRule.value?.copy(ringerAction = ringerAction)

        val item =
            RuleMemberItemViewModel(ringerActionRuleItem(ringerAction)).toRecyclerItem()
        _actionItemList.value =
            _actionItemList.value?.plus(item) ?: listOf(item)
    }

    private fun locationTriggerRuleItem(locationTrigger: LocationTrigger): RuleMemberItem =
        object : RuleMemberItem {
            override val title = "장소"
            override val description = locationTrigger.locationName
            override val layoutId = R.layout.item_trigger

            override fun delete(recyclerItem: RecyclerItem) {
                _triggerItemList.value = triggerItemList.value?.minus(recyclerItem)
                editingRule.value = editingRule.value?.copy(locationTrigger = null)
            }
        }

    private fun timeTriggerRuleItem(timeTrigger: TimeTrigger): RuleMemberItem =
        object : RuleMemberItem {
            override val title = "시간"
            override val description =
                "${minutesToString(timeTrigger.startTimeInMinutes)} - ${minutesToString(timeTrigger.endTimeInMinutes)}\n${
                    TimeUtils.repeatDayToString(timeTrigger.repeatDay)
                }"
            override val layoutId = R.layout.item_trigger

            override fun delete(recyclerItem: RecyclerItem) {
                _triggerItemList.value = triggerItemList.value?.minus(recyclerItem)
                editingRule.value = editingRule.value?.copy(timeTrigger = null)
            }
        }

    private fun activityTriggerRuleItem(activityTrigger: ActivityTrigger): RuleMemberItem =
        object : RuleMemberItem {
            override val title = "활동"
            override val description = activityTrigger.activity
            override val layoutId = R.layout.item_trigger

            override fun delete(recyclerItem: RecyclerItem) {
                _triggerItemList.value = triggerItemList.value?.minus(recyclerItem)
                editingRule.value = editingRule.value?.copy(activityTrigger = null)
            }
        }

    private fun appBlockActionRuleItem(appBlockAction: AppBlockAction): RuleMemberItem =
        object : RuleMemberItem {
            override val title = "앱 사용 제한"
            override val description = appBlockAction.AppBlockEntries.joinToString(", ") {
                "${it.appName} ${it.allowedTimeInMinutes}분 사용 시"
            }
            override val layoutId = R.layout.item_action

            override fun delete(recyclerItem: RecyclerItem) {
                _triggerItemList.value = triggerItemList.value?.minus(recyclerItem)
                editingRule.value = editingRule.value?.copy(appBlockAction = null)
            }
        }

    private fun notificationActionRuleItem(notificationAction: NotificationAction): RuleMemberItem =
        object : RuleMemberItem {
            override val title = "알림 처리"
            override val description = "${notificationAction.appList.joinToString("/")} 알림 숨김"
            override val layoutId = R.layout.item_action

            override fun delete(recyclerItem: RecyclerItem) {
                _triggerItemList.value = triggerItemList.value?.minus(recyclerItem)
                editingRule.value = editingRule.value?.copy(notificationAction = null)
            }
        }

    private fun dndActionRuleItem(dndAction: DndAction): RuleMemberItem =
        object : RuleMemberItem {
            override val title = "방해 금지 모드"
            override val description = "방해 금지 모드 실행"
            override val layoutId = R.layout.item_action

            override fun delete(recyclerItem: RecyclerItem) {
                _triggerItemList.value = triggerItemList.value?.minus(recyclerItem)
                editingRule.value = editingRule.value?.copy(dndAction = null)
            }
        }

    private fun ringerActionRuleItem(ringerAction: RingerAction): RuleMemberItem =
        object : RuleMemberItem {
            override val title = "소리 모드 변경"
            override val description: String = ringerAction.ringerMode.toString()   //TODO: Mapping
            override val layoutId = R.layout.item_action

            override fun delete(recyclerItem: RecyclerItem) {
                _triggerItemList.value = triggerItemList.value?.minus(recyclerItem)
                editingRule.value = editingRule.value?.copy(ringerAction = null)
            }
        }
}