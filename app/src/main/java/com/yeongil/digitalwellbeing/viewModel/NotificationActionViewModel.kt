package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import com.yeongil.digitalwellbeing.data.action.AppBlockEntry
import com.yeongil.digitalwellbeing.data.action.KeywordEntry
import com.yeongil.digitalwellbeing.data.action.NotificationAction
import com.yeongil.digitalwellbeing.repository.PackageManagerRepository
import com.yeongil.digitalwellbeing.utils.CLOSE_IMMEDIATE
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.viewModel.item.NotiAppItem
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.NotiAppItemViewModel
import kotlinx.coroutines.flow.collect

class NotificationActionViewModel(
    private val pmRepo: PackageManagerRepository
) : ViewModel() {
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
    private val onClickNotiAppItemDelete: (String) -> Unit = { id ->
        val oldList = notiAppList.value ?: listOf()
        notiAppList.value = oldList - id
    }

    val keywordItemList = MutableLiveData<List<RecyclerItem>>()
    // TODO: Make KeywordItem
    // TODO: Make KeywordItemViewModel
    // TODO: string to KeywordItem, to KeywordItemViewModel, to RecyclerItem

    // TODO: Bind with fragment_notification_action
    // TODO: Add keywordItemClickEvent
    // TODO: Add keywordItemClickInclusionEvent
    // TODO: Add keywordItemClickDeleteEvent

    val handlingAction = MutableLiveData<Int>()
    // TODO: Bind with a spinner in fragment_notification_action

    fun init() {
        notiAppList.value = listOf()
        keywordItemList.value = listOf()
    }

    fun init(notificationAction: NotificationAction) {
        notificationAction.appList
        notificationAction.keywordList
        notificationAction.handlingAction
    }

    fun getNotificationAction() {
        val appList = notiAppList.value ?: listOf()
//        val keywordEntryList = keywordList.value ?: listOf()
        // TODO: keywordItemList to keywordEntryList
        val handlingAction = this.handlingAction.value ?: 0

//        return NotificationAction(appList, keywordList, handlingAction)
    }

    fun setAppList(packageNameList: List<String>) {
        notiAppList.value = packageNameList
    }

    fun addKeyword(keyword: String) {
        // TODO: Add or update a keyword
    }
}