package com.yeongil.digitalwellbeing.viewModel.viewModel.rule

import android.text.Html
import android.text.Spanned
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
import com.yeongil.digitalwellbeing.utils.TimeUtils.repeatDayToString
import com.yeongil.digitalwellbeing.utils.TimeUtils.startEndMinutesToString
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

    /* For Confirm Fragment */
    private val redFontTagStart = "<font color=\"#A52A2A\">"
    private val redFontTagEnd = "</font>"
    private val breakTag = "<br />"

    /* Trigger Html */
    val locationHtml = editingRule.map { rule ->
        val location = rule.locationTrigger?.locationName ?: ""
        val range = rule.locationTrigger?.range?.toString()?.let { "${it}m" } ?: ""
        val conjunction = if (rule.timeTrigger != null) "(그리고)" else ""

        val html = "$redFontTagStart$location${redFontTagEnd}을(를)$breakTag" +
                "중심으로 ${redFontTagStart}${range} 이내${redFontTagEnd} $conjunction"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        else Html.fromHtml(html)
    }
    val timeHtml = editingRule.map { rule ->
        val repeatDay = rule.timeTrigger?.repeatDay?.let { repeatDayToString(it) }
        val time = rule.timeTrigger?.let {
            startEndMinutesToString(it.startTimeInMinutes, it.endTimeInMinutes)
        }
        val conjunction = if (rule.activityTrigger != null) "(그리고)" else ""

        val html = "${redFontTagStart}매주 ${repeatDay}요일$breakTag" +
                "$time${redFontTagEnd} $conjunction"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        else Html.fromHtml(html)
    }
    val activityHtml = editingRule.map { rule ->
        val activity = when (rule.activityTrigger?.activity) {
            DRIVE -> "자동차 운전"
            BICYCLE -> "자전거 운행"
            STILL -> "아무것도 하지 않을 때"
            else -> ""
        }

        val html = "$redFontTagStart$activity$redFontTagEnd 감지"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        else Html.fromHtml(html)
    }
    val manualTriggerHtml: Spanned = run {
        val html = "${redFontTagStart}사용자가 규칙을 활성화 시${redFontTagEnd}"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        else Html.fromHtml(html)
    }
    val isManualTrigger = editingRule.map {
        it.locationTrigger == null && it.activityTrigger == null && it.timeTrigger == null
    }

    /* Action Html */
    val actionHtml = editingRule.map { rule ->
        val ringerHtml = rule.ringerAction?.let {
            "소리 모드: " +
                    when (it.ringerMode) {
                        RingerAction.RingerMode.VIBRATE -> "진동으로 변경"
                        RingerAction.RingerMode.RING -> "소리로 변경"
                        RingerAction.RingerMode.SILENT -> "무음으로 변경"
                    }
        }

        val dndHtml = rule.dndAction?.let {
            "방해 금지 모드: 켜기" + if (ringerHtml != null) breakTag else ""
        }

        val notificationHtml = rule.notificationAction?.let { action ->
            val header = "알림 숨김:"
            val appText =
                if (action.allApp)
                    "모든 앱에서 발생한 알림 중"
                else
                    action.appList.joinToString(", ") { pmRepo.getLabel(it) } +
                            " 앱에서 발생한 알림 중,"
            val keywordText =
                action.keywordList.joinToString("하거나$breakTag") {
                    val inclusion = if (it.inclusion) "포함" else "미포함"
                    "${it.keyword}을(를) $inclusion"
                } + "한 알림은 숨김"
            val end = if (dndHtml != null || ringerHtml != null) breakTag else ""

            "$header$breakTag$appText$breakTag$keywordText$end"
        }

        val appBlockHtml = rule.appBlockAction?.let { action ->
            val header = "앱 제한:"
            val content = if (action.allAppBlock) {
                val actionType = if (action.allAppHandlingAction == CLOSE_IMMEDIATE) "종료" else "경고"

                "모든 앱 실행 시 $actionType"
            } else {
                action.appBlockEntryList.joinToString(breakTag) {
                    val actionType = if (it.handlingAction == CLOSE_IMMEDIATE) "종료" else "경고"
                    val appName = pmRepo.getLabel(it.packageName)
                    val allowedTime =
                        if (it.allowedTimeInMinutes == 0) "실행 시"
                        else "${TimeUtils.minutesToTimeMinute(it.allowedTimeInMinutes)} 이상 사용 시"

                    "$appName $allowedTime $actionType"
                }
            }
            val end =
                if (dndHtml != null || ringerHtml != null || notificationHtml != null)
                    breakTag else ""

            "$header$breakTag$content$end"
        }

        val html =
            listOfNotNull(appBlockHtml, notificationHtml, dndHtml, ringerHtml)
                .joinToString(breakTag)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        else Html.fromHtml(html)
    }

    /* Releasing Html */
    val locationReleasingHtml = editingRule.map { rule ->
        val location = rule.locationTrigger?.locationName ?: ""
        val range = rule.locationTrigger?.range?.toString()?.let { "${it}m" } ?: ""
        val conjunction = if (rule.timeTrigger != null) "(또는)" else ""

        val html = "$redFontTagStart$location${redFontTagEnd}을(를)$breakTag" +
                "중심으로 ${redFontTagStart}${range} 벗어남${redFontTagEnd} $conjunction"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        else Html.fromHtml(html)
    }
    val timeReleasingHtml = editingRule.map { rule ->
        val repeatDay = rule.timeTrigger?.repeatDay?.let { repeatDayToString(it) }
        val endTime = rule.timeTrigger?.let {
            startEndMinutesToString(it.startTimeInMinutes, it.endTimeInMinutes)
        }?.split(" - ")?.get(1)
        val conjunction = if (rule.activityTrigger != null) "(또는)" else ""

        val html = "${redFontTagStart}매주 ${repeatDay}요일$breakTag" +
                "$endTime 이후${redFontTagEnd} $conjunction"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        else Html.fromHtml(html)
    }
    val activityReleasingHtml = editingRule.map { rule ->
        val activity = when (rule.activityTrigger?.activity) {
            DRIVE -> "자동차 운전"
            BICYCLE -> "자전거 운행"
            STILL -> "아무것도 하지 않을 때"
            else -> ""
        }

        val html = "$activity 외 ${redFontTagStart}다른 활동$redFontTagEnd 감지"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        else Html.fromHtml(html)
    }
    val manualReleasingHtml: Spanned = run {
        val html = "${redFontTagStart}사용자가 규칙을 해제 시${redFontTagEnd}"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        else Html.fromHtml(html)
    }

    /* Function */

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