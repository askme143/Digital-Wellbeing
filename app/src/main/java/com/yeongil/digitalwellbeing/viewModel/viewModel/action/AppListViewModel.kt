package com.yeongil.digitalwellbeing.viewModel.viewModel.action

import androidx.lifecycle.*
import com.yeongil.digitalwellbeing.repository.PackageManagerRepository
import com.yeongil.digitalwellbeing.utils.combineWith
import com.yeongil.digitalwellbeing.viewModel.item.AppItem
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.AppItemViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppListViewModel(
    private val pmRepo: PackageManagerRepository
) : ViewModel() {
    // Data
    private val appItemList = MutableLiveData<List<AppItem>>()
    val searchText = MutableLiveData<String>()
    val appItemAllChecked = MutableLiveData<Boolean>(false)

    // View Related
    val appRecyclerItemList = appItemList.map { list ->
        list.map {
            AppItemViewModel(
                it.packageName,
                it,
                appItemAllChecked,
                onClickItem
            ).toRecyclerItem()
        }
    }
    val appRecyclerItemListSearched =
        searchText
            .combineWith(appRecyclerItemList) { text, list -> Pair(text, list) }
            .map { (searchText, recyclerItemList) ->
                val text = searchText ?: ""
                val list = recyclerItemList ?: listOf()

                list.map { it.viewModel }
                    .filterIsInstance<AppItemViewModel>()
                    .filter { it.appItem.label.contains(text, true) }
                    .map { it.toRecyclerItem() }
            }

    val itemCount = MutableLiveData<Int>(0)
    val onClickItem: (MutableLiveData<Boolean>) -> Unit = {
        val newValue = !(it.value ?: false)
        it.value = newValue
        itemCount.value = (itemCount.value ?: 0) + (if (newValue) 1 else -1)
    }

    fun onClickAll() {
        val oldCount = itemCount.value ?: 0
        if (appItemAllChecked.value == true) itemCount.value = oldCount + 1
        else itemCount.value = oldCount - 1
    }

    ////////////////////////////////////////
    /* View Model Communication Functions */
    ////////////////////////////////////////

    /* TODO: Change to use cache in repository pattern */
    fun loadAppList() {
        viewModelScope.launch(Dispatchers.Default) {
            val oldCheckedList = appItemList.value?.filter { it.checked.value == true } ?: listOf()
            val freshList = pmRepo.getAppInfoList()
                .filter { !pmRepo.isSystemApp(it) }
                .map { AppItem(it.packageName, pmRepo.getLabel(it), pmRepo.getIcon(it)) }
                .sortedBy { it.label }
                .toMutableList()

            withContext(Dispatchers.Main) {
                oldCheckedList.forEach { oldItem ->
                    val idx = freshList.indexOfFirst { it.packageName == oldItem.packageName }
                    if (idx != -1) {
                        freshList[idx].checked.value = oldItem.checked.value
                    }
                }

                appItemList.value = freshList
            }
        }
    }

    fun putAppList(appList: List<String>) {
        loadAppList()

        itemCount.value = appList.size
        appItemAllChecked.value = false
        appItemList.value?.forEach {
            it.checked.value = appList.contains(it.packageName)
        }
    }

    fun getAppList(): List<String>? {
        return if (appItemAllChecked.value == true) null
        else appItemList.value!!.filter { it.checked.value == true }.map { it.packageName }
    }

    fun putAllApp() {
        loadAppList()

        itemCount.value = 1
        appItemAllChecked.value = true
        appItemList.value!!.forEach { it.checked.value = false }
    }
}