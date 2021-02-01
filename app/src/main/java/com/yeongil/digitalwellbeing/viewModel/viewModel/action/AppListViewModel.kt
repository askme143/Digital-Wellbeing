package com.yeongil.digitalwellbeing.viewModel.viewModel.action

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.yeongil.digitalwellbeing.repository.PackageManagerRepository
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.viewModel.item.AppItem
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.AppItemViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine

class AppListViewModel(
    private val pmRepo: PackageManagerRepository
) : ViewModel() {
    val appItemList = MutableLiveData<List<RecyclerItem>>()
    val appItemAllChecked = MutableLiveData<Boolean>(null)

    val onClickItem: (Boolean) -> Unit = {
        itemCount.value = itemCount.value?.plus(if (it) 1 else -1)
    }
    val itemCount = MutableLiveData<Int>(0)

    @SuppressLint("NullSafeMutableLiveData")
    fun init() {
        itemCount.value = 0

        appItemAllChecked.value = null
        appItemList.value = pmRepo.getAppInfoList()
            .filter { !pmRepo.isSystemApp(it) }
            .map {
                Pair(
                    it.packageName,
                    AppItem(it.packageName, pmRepo.getLabel(it), pmRepo.getIcon(it))
                )
            }
            .sortedBy { it.second.label }
            .map { AppItemViewModel(it.first, it.second, onClickItem).toRecyclerItem() }
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun init(appList: List<String>) {
        itemCount.value = appList.size

        appItemAllChecked.value = null
        appItemList.value = pmRepo.getAppInfoList()
            .filter { !pmRepo.isSystemApp(it) }
            .map {
                AppItem(
                    it.packageName,
                    pmRepo.getLabel(it),
                    pmRepo.getIcon(it),
                    MutableLiveData(appList.contains(it.packageName))
                )
            }
            .sortedBy { it.label }
            .map { AppItemViewModel(it.packageName, it, onClickItem).toRecyclerItem() }
    }

    fun getCheckedAppList(): List<String> {
        return appItemList.value!!
            .map { it.viewModel }
            .filterIsInstance<AppItemViewModel>()
            .filter { it.appItem.checked.value == true }
            .map { it.appItem.packageName }
    }
}