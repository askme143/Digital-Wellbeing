package com.yeongil.digitalwellbeing.viewModel.viewModel.action

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.yeongil.digitalwellbeing.repository.PackageManagerRepository
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.viewModel.item.AppItem
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.AppItemViewModel

class AppListViewModel(
    private val pmRepo: PackageManagerRepository
) : ViewModel() {
    val appRecyclerItemList = MutableLiveData<List<RecyclerItem>>()
    val appItemAllChecked = MutableLiveData<Boolean>(false)
    fun onClickAll() {
        val oldCount = itemCount.value ?: 0
        if (appItemAllChecked.value == true) itemCount.value = oldCount + 1
        else itemCount.value = oldCount - 1
    }

    val onClickItem: (Boolean) -> Unit = {
        itemCount.value = itemCount.value?.plus(if (it) 1 else -1)
    }
    val itemCount = MutableLiveData<Int>(0)

    fun init() {
        itemCount.value = 0

        appItemAllChecked.value = false
        appRecyclerItemList.value = pmRepo.getAppInfoList()
            .filter { !pmRepo.isSystemApp(it) }
            .map {
                Pair(
                    it.packageName,
                    AppItem(it.packageName, pmRepo.getLabel(it), pmRepo.getIcon(it))
                )
            }
            .sortedBy { it.second.label }
            .map {
                AppItemViewModel(
                    it.first,
                    it.second,
                    appItemAllChecked,
                    onClickItem
                ).toRecyclerItem()
            }
    }

    fun init(appList: List<String>) {
        itemCount.value = appList.size

        appItemAllChecked.value = false
        appRecyclerItemList.value = pmRepo.getAppInfoList()
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
            .map {
                AppItemViewModel(
                    it.packageName,
                    it,
                    appItemAllChecked,
                    onClickItem
                ).toRecyclerItem()
            }
    }

    fun init(allApp: Boolean) {
        init()
        if (allApp) {
            itemCount.value = 1
            appItemAllChecked.value = true
        }
    }

    fun getCheckedAppList(): List<String> {
        return appRecyclerItemList.value!!
            .map { it.viewModel }
            .filterIsInstance<AppItemViewModel>()
            .filter { it.appItem.checked.value == true }
            .map { it.appItem.packageName }
    }

    fun isAllAppChecked() = appItemAllChecked.value ?: false
}