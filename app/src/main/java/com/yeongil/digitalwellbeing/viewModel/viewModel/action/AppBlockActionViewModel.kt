package com.yeongil.digitalwellbeing.viewModel.viewModel.action

import androidx.lifecycle.*
import com.yeongil.digitalwellbeing.data.rule.action.AppBlockAction
import com.yeongil.digitalwellbeing.data.rule.action.AppBlockEntry
import com.yeongil.digitalwellbeing.repository.PackageManagerRepository
import com.yeongil.digitalwellbeing.utils.ALERT
import com.yeongil.digitalwellbeing.utils.CLOSE_IMMEDIATE
import com.yeongil.digitalwellbeing.utils.Event
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.viewModel.item.AppBlockEntryItem
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.AllAppBlockEntryItemViewModel
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.AppBlockEntryItemViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine

class AppBlockActionViewModel(
    private val pmRepo: PackageManagerRepository
) : ViewModel() {
    /* Data */
    var originalAction: AppBlockAction? = null
    val appBlockEntryList = MutableLiveData<List<AppBlockEntry>>()
    val allAppBlock = MutableLiveData<Boolean>(false)
    val allAppHandlingAction = MutableLiveData<Int>(CLOSE_IMMEDIATE)

    /* View Related */
    // Recycler Item List
    val allAppBlockEntryItemList = liveData<List<RecyclerItem>> {
        allAppHandlingAction.asFlow().collect {
            val description = when (it) {
                CLOSE_IMMEDIATE -> "바로 종료"
                ALERT -> "경고 알림"
                else -> ""
            }
            emit(
                listOf(
                    AllAppBlockEntryItemViewModel(
                        description = description,
                        onClickItem = onAllAppItemClick,
                        onClickDeleteItem = onAllAppItemDelete
                    ).toRecyclerItem()
                )
            )
        }
    }
    val appBlockEntryItemList = liveData<List<RecyclerItem>> {
        appBlockEntryList.asFlow().collect { list ->
            emit(
                list.map { AppBlockEntryItem(it, pmRepo) }
                    .map {
                        AppBlockEntryItemViewModel(
                            it.packageName,
                            it,
                            onClickItem,
                            onClickItemDelete
                        )
                    }
                    .map { it.toRecyclerItem() }
            )
        }
    }

    // Action Exists; for blocking complete button
    val actionExists = liveData<Boolean> {
        appBlockEntryList.asFlow().combine(allAppBlock.asFlow()) { list, bool ->
            Pair(list, bool)
        }.collect { (list, allAppBlock) ->
            emit(list.isNotEmpty() || allAppBlock)
        }
    }

    // item click
    val itemClickEvent = MutableLiveData<Event<AppBlockEntry>>()
    private val onClickItem: (String) -> Unit = { packageName ->
        itemClickEvent.value = Event(getAppBlockEntry(packageName))
    }

    // item delete click
    private val onClickItemDelete: (String) -> Unit = { packageName ->
        val oldEntryList = appBlockEntryList.value ?: listOf()
        val index = oldEntryList.map { it.packageName }.indexOf(packageName)
        appBlockEntryList.value = oldEntryList - oldEntryList[index]
    }

    // all app item click
    val allAppItemClickEvent = MutableLiveData<Event<Int>>()
    private val onAllAppItemClick: () -> Unit =
        { allAppItemClickEvent.value = Event(getAllAppHandlingAction()) }

    // all app item delete
    private val onAllAppItemDelete: () -> Unit = { allAppBlock.value = false }

    ///////////////
    /* Rule Edit */
    ///////////////
    fun getAppBlockAction(): AppBlockAction? {
        val list = appBlockEntryList.value ?: listOf()
        val allApp = allAppBlock.value ?: false
        val handlingAction = allAppHandlingAction.value!!

        return if (list.isEmpty() && !allApp) null
        else AppBlockAction(list, allApp, handlingAction)
    }

    fun putAppBlockAction(action: AppBlockAction?) {
        originalAction = action
        allAppBlock.value = action?.allAppBlock ?: false
        allAppHandlingAction.value = action?.allAppHandlingAction ?: CLOSE_IMMEDIATE
        appBlockEntryList.value = action?.appBlockEntryList ?: listOf()
    }

    //////////////
    /* App List */
    //////////////
    fun getAppList(): List<String>? {
        return if (allAppBlock.value == true) null
        else appBlockEntryList.value?.map { it.packageName } ?: listOf()
    }

    fun updateAppBlockEntryList(appList: List<String>) {
        val oldList = appBlockEntryList.value ?: listOf()
        val subtractedList = oldList.filter { appList.contains(it.packageName) }
        val subtractedAppList = subtractedList.map { it.packageName }
        val additionList = appList
            .filterNot { subtractedAppList.contains(it) }
            .map { AppBlockEntry(it, 0, CLOSE_IMMEDIATE) }

        appBlockEntryList.value = subtractedList + additionList
        allAppBlock.value = false
    }

    fun putAllApp() {
        appBlockEntryList.value = listOf()
        allAppBlock.value = true
        allAppHandlingAction.value = CLOSE_IMMEDIATE
    }

    /////////////////////
    /* App Block Entry */
    /////////////////////
    fun updateAllAppHandlingAction(handlingAction: Int) {
        allAppHandlingAction.value = handlingAction
    }

    private fun getAllAppHandlingAction(): Int {
        return allAppHandlingAction.value ?: CLOSE_IMMEDIATE
    }

    private fun getAppBlockEntry(packageName: String): AppBlockEntry {
        return appBlockEntryList.value!!.last { it.packageName == packageName }
    }

    fun updateAppBlockEntryList(entry: AppBlockEntry) {
        with(appBlockEntryList) {
            val index = value!!.indexOfFirst { it.packageName == entry.packageName }
            value = value!!.toMutableList().apply { this[index] = entry }
        }
    }
}