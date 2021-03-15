package com.yeongil.digitalwellbeing.viewModel.viewModel.action

import androidx.lifecycle.*
import com.yeongil.digitalwellbeing.data.rule.action.KeywordEntry
import com.yeongil.digitalwellbeing.data.rule.action.NotificationAction
import com.yeongil.digitalwellbeing.repository.PackageManagerRepository
import com.yeongil.digitalwellbeing.utils.*
import com.yeongil.digitalwellbeing.viewModel.item.NotiKeywordItem
import com.yeongil.digitalwellbeing.viewModel.item.NotiAppItem
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.NotiAllAppItemViewModel
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.NotiKeywordItemViewModel
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.NotiAppItemViewModel

class NotificationActionViewModel(
    private val pmRepo: PackageManagerRepository
) : ViewModel() {
    var originalAction: NotificationAction? = null

    /* Handling Action */
    val constNotificationHide = NOTIFICATION_HIDE
    val constNotificationVibrate = NOTIFICATION_VIBRATE
    val constNotificationRing = NOTIFICATION_RING
    val constNotificationSilent = NOTIFICATION_SILENT
    val handlingAction = MutableLiveData<Int>()
    val handlingActionText = handlingAction.map {
        when (it) {
            NOTIFICATION_HIDE -> "알림 숨기기"
            NOTIFICATION_VIBRATE -> "진동으로 받기"
            NOTIFICATION_RING -> "소리로 받기"
            NOTIFICATION_SILENT -> "무음으로 받기"
            else -> ""
        }
    }

    fun onSelectHandlingAction(action: Int) = run { handlingAction.value = action }

    /* App */
    /* App List */
    val appList = MutableLiveData<List<String>>()
    val notiAppRecyclerItemList = appList.map { list ->
        list.map { NotiAppItem(it, pmRepo) }
            .map { NotiAppItemViewModel(it.packageName, it, onClickNotiAppItemDelete) }
            .map { it.toRecyclerItem() }
    }
    private val onClickNotiAppItemDelete: (String) -> Unit = {
        appList.value = appList.value?.minus(it) ?: listOf()
    }

    /* All App */
    val allApp = MutableLiveData<Boolean>()
    private val onClickAllAppItemDelete = { allApp.value = false }
    val allAppRecyclerItemList = MutableLiveData(
        listOf(
            NotiAllAppItemViewModel("ALL_APP", onClickAllAppItemDelete).toRecyclerItem()
        )
    )

    /* No App */
    val noApp = appList
        .combineWith(allApp) { first, second -> Pair(first, second) }
        .map { (appList, allApp) -> allApp?.not() ?: true && appList?.isEmpty() ?: true }

    /* Noti Keyword */
    val notiKeywordItemList = MutableLiveData<List<NotiKeywordItem>>()
    val notiKeywordItemClickEvent = MutableLiveData<Event<NotiKeywordItem>>()
    private val onClickNotiKeywordItem = { id: String ->
        notiKeywordItemClickEvent.value = Event(getNotiKeywordItem(id))
    }
    private val onClickNotiKeywordItemDelete = { id: String ->
        val oldList = notiKeywordItemList.value ?: listOf()
        notiKeywordItemList.value = oldList.filterNot { it.id == id }
    }
    val notiKeywordRecyclerItemList = notiKeywordItemList.map { list ->
        list.map {
            NotiKeywordItemViewModel(
                it.id,
                it,
                onClickNotiKeywordItem,
                onClickNotiKeywordItemDelete
            ).toRecyclerItem()
        }
    }

    /* No Keyword */
    val noKeyword = notiKeywordItemList.map { it.isEmpty() }

    /* Action Exists: if not, block the complete button*/
    val actionExists =
        appList.combineWith(allApp) { list, bool ->
            (list?.isNotEmpty() ?: false) || (bool ?: false)
        }

    //////////////////////////
    /* Rule Edit View Model */
    //////////////////////////
    fun putNotificationAction(action: NotificationAction?) {
        originalAction = action

        appList.value = action?.appList ?: listOf()
        allApp.value = action?.allApp ?: false
        notiKeywordItemList.value = action?.keywordList
            ?.mapIndexed { index, entry ->
                NotiKeywordItem(
                    index.toString(),
                    entry.keyword,
                    MutableLiveData(entry.inclusion)
                )
            } ?: listOf()
        handlingAction.value = action?.handlingAction ?: constNotificationHide
    }

    fun getNotificationAction(): NotificationAction? {
        val appList = appList.value ?: listOf()
        val allApp = allApp.value ?: false
        val keywordEntryList = notiKeywordItemList.value?.map {
            KeywordEntry(it.keyword, it.inclusion.value!!)
        } ?: listOf()
        val handingAction = handlingAction.value ?: constNotificationHide

        return if (appList.isEmpty() && !allApp) null
        else NotificationAction(appList, allApp, keywordEntryList, handingAction)
    }

    /////////////////////////
    /* App List View Model */
    /////////////////////////
    fun getAppList(): List<String>? {
        return if (allApp.value!!) null
        else appList.value ?: listOf()
    }

    fun updateAppList(appList: List<String>) {
        this.appList.value = appList
        allApp.value = false
    }

    fun putAllApp() {
        appList.value = listOf()
        allApp.value = true
    }

    //////////////////////////
    /* Notification Keyword */
    //////////////////////////
    fun getNotiKeywordItem(id: String): NotiKeywordItem {
        return notiKeywordItemList.value!!.first { it.id == id }
    }

    fun putKeywordItem(item: NotiKeywordItem) {
        val oldList = notiKeywordItemList.value ?: listOf()
        val index = oldList.indexOfLast { it.id == item.id }
        if (index == -1) notiKeywordItemList.value = oldList + item
        else notiKeywordItemList.value = oldList.toMutableList().apply { this[index] = item }
    }
}