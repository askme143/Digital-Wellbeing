package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import com.yeongil.digitalwellbeing.data.action.KeywordEntry
import com.yeongil.digitalwellbeing.data.action.NotificationAction
import com.yeongil.digitalwellbeing.repository.PackageManagerRepository
import com.yeongil.digitalwellbeing.utils.*
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.viewModel.item.KeywordItem
import com.yeongil.digitalwellbeing.viewModel.item.NotiAppItem
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.KeywordItemViewModel
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.NotiAppItemViewModel
import kotlinx.coroutines.flow.collect

class NotificationActionViewModel(
    private val pmRepo: PackageManagerRepository
) : ViewModel() {
    var editing = false

    val notiAppList = MutableLiveData<List<String>>()
    val notiAppItemList = liveData<List<RecyclerItem>> {
        notiAppList.asFlow().collect { list ->
            emit(
                list.map { NotiAppItem(it, pmRepo.getLabel(it), pmRepo.getIcon(it)) }
                    .map { NotiAppItemViewModel(it.packageName, it, onClickNotiAppItemDelete) }
                    .map { it.toRecyclerItem() }
            )
        }
    }

    val keywordItemList = MutableLiveData<List<RecyclerItem>>()
    val keywordItemClickEvent = MutableLiveData<Event<String>>()

    val constNotificationHide = NOTIFICATION_HIDE
    val constNotificationVibrate = NOTIFICATION_VIBRATE
    val constNotificationRing = NOTIFICATION_RING
    val constNotificationSilent = NOTIFICATION_SILENT
    val handlingAction = MutableLiveData<Int>()
    val handlingActionText = liveData<String> {
        handlingAction.asFlow().collect {
            emit(
                when (it) {
                    NOTIFICATION_HIDE -> "숨김"
                    NOTIFICATION_VIBRATE -> "진동"
                    NOTIFICATION_RING -> "소리"
                    NOTIFICATION_SILENT -> "무음"
                    else -> ""
                }
            )
        }
    }

    fun onHandlingSelected(action: Int) = run { handlingAction.value = action }

    fun init() {
        notiAppList.value = listOf()
        keywordItemList.value = listOf()
        handlingAction.value = constNotificationHide
    }

    fun init(action: NotificationAction) {
        notiAppList.value = action.appList
        keywordItemList.value = action.keywordList
            .mapIndexed { index, entry ->
                KeywordItem(index.toString(), entry.keyword, MutableLiveData(entry.inclusion))
            }
            .map {
                KeywordItemViewModel(
                    it.id,
                    it,
                    onClickKeywordItem,
                    onClickKeywordDelete
                ).toRecyclerItem()
            }
        handlingAction.value = action.handlingAction
    }

    fun getNotificationAction(): NotificationAction {
        val appList = notiAppList.value ?: listOf()
        val keywordItemList = this.keywordItemList.value ?: listOf()
        val keywordEntryList =
            keywordItemList.map { it.viewModel }
                .filterIsInstance<KeywordItemViewModel>()
                .map { it.keywordItem }
                .map { KeywordEntry(it.keyword, it.inclusion.value ?: true) }
        val handlingAction = this.handlingAction.value ?: constNotificationHide

        return NotificationAction(appList, keywordEntryList, handlingAction)
    }

    fun setAppList(packageNameList: List<String>) {
        notiAppList.value = packageNameList
    }

    fun addKeywordItem(item: KeywordItem) {
        val oldList = keywordItemList.value ?: listOf()
        val index = oldList.map { it.viewModel }
            .filterIsInstance<KeywordItemViewModel>()
            .map { it.id }
            .indexOf(item.id)
        val newRecyclerItem =
            KeywordItemViewModel(
                item.id,
                item,
                onClickKeywordItem,
                onClickKeywordDelete
            ).toRecyclerItem()

        if (index == -1) {
            keywordItemList.value = oldList + newRecyclerItem
        } else {
            val size = oldList.size
            keywordItemList.value =
                oldList.subList(0, index) + newRecyclerItem + oldList.subList(index + 1, size)
        }
    }

    private val onClickNotiAppItemDelete: (String) -> Unit = { id ->
        val oldList = notiAppList.value ?: listOf()
        notiAppList.value = oldList - id
    }

    private val onClickKeywordItem: (String) -> Unit = { keywordItemClickEvent.value = Event(it) }

    private val onClickKeywordDelete: (String) -> Unit = { id ->
        val oldList = keywordItemList.value ?: listOf()
        val index = oldList.map { it.viewModel }
            .filterIsInstance<KeywordItemViewModel>()
            .map { it.id }
            .indexOf(id)

        keywordItemList.value = oldList - oldList[index]
    }
}