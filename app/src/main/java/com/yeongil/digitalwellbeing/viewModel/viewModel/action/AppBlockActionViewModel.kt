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
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.AppBlockEntryItemViewModel
import kotlinx.coroutines.flow.collect

class AppBlockActionViewModel(
    private val pmRepo: PackageManagerRepository
) : ViewModel() {
    var editing = false

    val appBlockEntryList = MutableLiveData<List<AppBlockEntry>>()
    val appBlockEntryItemList = liveData<List<RecyclerItem>> {
        appBlockEntryList.asFlow().collect { list ->
            emit(
                list.map { appBlockEntryItem(it) }
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

    val itemClickDeleteEvent = MutableLiveData<Event<String>>()
    private val onClickEntryItemDelete: (String) -> Unit = { packageName ->
        itemClickDeleteEvent.value = Event(packageName)
    }

    val itemClickEvent = MutableLiveData<Event<String>>()
    private val onClickEntryItem: (String) -> Unit = { packageName ->
        itemClickEvent.value = Event(packageName)
    }

    fun init() {
        appBlockEntryList.value = listOf()
    }

    fun init(appBlockAction: AppBlockAction) {
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

    fun getAppBlockAction() = AppBlockAction(appBlockEntryList.value!!)

    private fun appBlockEntryItem(entry: AppBlockEntry): AppBlockEntryItem {
        val packageName = entry.packageName
        val label = pmRepo.getLabel(packageName)
        val icon = pmRepo.getIcon(packageName)
        val action = when (entry.handlingAction) {
            CLOSE_IMMEDIATE -> "바로 종료"
            ALERT -> "경고 알림"
            else -> ""
        }
        val description = "${entry.allowedTimeInMinutes} 분 후 $action"

        return AppBlockEntryItem(packageName, label, icon, description)
    }
}