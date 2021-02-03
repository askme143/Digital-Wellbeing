package com.yeongil.digitalwellbeing.viewModel.viewModel.action

import androidx.lifecycle.*
import com.yeongil.digitalwellbeing.data.action.AppBlockAction
import com.yeongil.digitalwellbeing.data.action.AppBlockEntry
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
    var editing = false

    val appBlockEntryList = MutableLiveData<List<AppBlockEntry>>()
    val allAppBlock = MutableLiveData<Boolean>(false)
    val allAppHandlingAction = MutableLiveData<Int>(CLOSE_IMMEDIATE)

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
                        onClickItem = onAllItemClick,
                        onClickDeleteItem = onAllItemDelete
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
                            onClickEntryItem,
                            onClickEntryItemDelete
                        )
                    }
                    .map { it.toRecyclerItem() }
            )
        }
    }

    val isAppBlockListEmpty = liveData<Boolean> {
        appBlockEntryList.asFlow().collect { emit(it.isEmpty()) }
    }

    private val onClickEntryItemDelete: (String) -> Unit = { packageName ->
        val oldEntryList = appBlockEntryList.value ?: listOf()
        val index = oldEntryList.map { it.packageName }.indexOf(packageName)
        appBlockEntryList.value = oldEntryList - oldEntryList[index]
    }

    val itemClickEvent = MutableLiveData<Event<String>>()
    private val onClickEntryItem: (String) -> Unit = { packageName ->
        itemClickEvent.value = Event(packageName)
    }

    val allItemClickEvent = MutableLiveData<Event<Unit>>()
    private val onAllItemClick: () -> Unit = { allItemClickEvent.value = Event(Unit) }
    private val onAllItemDelete: () -> Unit = { allAppBlock.value = false }

    fun init() {
        allAppBlock.value = false
        allAppHandlingAction.value = CLOSE_IMMEDIATE
        appBlockEntryList.value = listOf()
    }

    fun init(appBlockAction: AppBlockAction) {
        allAppBlock.value = appBlockAction.allAppBlock
        allAppHandlingAction.value = appBlockAction.allAppHandlingAction
        appBlockEntryList.value = appBlockAction.appBlockEntryList
    }

    fun setAppList(packageNameList: List<String>) {
        val oldEntryList = appBlockEntryList.value ?: listOf()
        val subtractedEntryList = oldEntryList.filter { packageNameList.contains(it.packageName) }
        val subtractedAppList = subtractedEntryList.map { it.packageName }
        val additionEntryList = packageNameList
            .filterNot { subtractedAppList.contains(it) }
            .map { AppBlockEntry(it, 0, CLOSE_IMMEDIATE) }

        appBlockEntryList.value = subtractedEntryList + additionEntryList
    }

    fun setAllApp() {
        allAppBlock.value = true
        allAppHandlingAction.value = CLOSE_IMMEDIATE
        appBlockEntryList.value = listOf()
    }

    fun addAppBlockEntry(entry: AppBlockEntry) {
        val existingAppList = appBlockEntryList.value?.map { it.packageName } ?: listOf()
        val index = existingAppList.indexOf(entry.packageName)
        if (index == -1) {
            appBlockEntryList.value = appBlockEntryList.value?.plus(entry) ?: listOf(entry)
        } else {
            val oldList = appBlockEntryList.value!!
            val size = oldList.size
            appBlockEntryList.value =
                oldList.subList(0, index) + entry + oldList.subList(index + 1, size)
        }
    }

    fun getAppBlockAction() =
        AppBlockAction(appBlockEntryList.value!!, allAppBlock.value!!, allAppHandlingAction.value!!)
}