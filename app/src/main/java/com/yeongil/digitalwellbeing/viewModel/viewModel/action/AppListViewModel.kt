package com.yeongil.digitalwellbeing.viewModel.viewModel.action

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.yeongil.digitalwellbeing.repository.PackageManagerRepository
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.viewModel.item.AppItem
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.AppItemViewModel
import kotlinx.coroutines.flow.collect

class AppListViewModel(
    private val pmRepo: PackageManagerRepository
) : ViewModel() {
    // Data
    private val appItemList = MutableLiveData<List<AppItem>>()
    val appItemAllChecked = MutableLiveData<Boolean>(false)

    // View Related
    val appRecyclerItemList = liveData {
        appItemList.asFlow().collect { list ->
            emit(
                list.map {
                    AppItemViewModel(
                        it.packageName,
                        it,
                        appItemAllChecked,
                        onClickItem
                    ).toRecyclerItem()
                })
        }
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

    private fun loadAppList() {
        if (appItemList.value == null)
            appItemList.value = pmRepo.getAppInfoList()
                .filter { !pmRepo.isSystemApp(it) }
                .map { AppItem(it.packageName, pmRepo.getLabel(it), pmRepo.getIcon(it)) }
                .sortedBy { it.label }
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