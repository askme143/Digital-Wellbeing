package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.data.action.AppBlockAction
import com.yeongil.digitalwellbeing.repository.PackageManagerRepository
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.viewModel.item.AppItem
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.AppItemViewModel

class AppListViewModel(
    private val pmRepo: PackageManagerRepository
) : ViewModel() {
    val appItemList = MutableLiveData<List<RecyclerItem>>()
    val appItemAllChecked = MutableLiveData<Boolean?>()

    fun init() {
        appItemList.value = pmRepo.getAppInfoList()
            .filter { !pmRepo.isSystemApp(it) }
            .map {
                Pair(
                    it.packageName,
                    AppItem(it.packageName, pmRepo.getLabel(it), pmRepo.getIcon(it))
                )
            }
            .map { AppItemViewModel(it.first, it.second).toRecyclerItem() }
    }

    fun init(appBlockAction: AppBlockAction) {
        val existingAppList = appBlockAction.appBlockEntryList.map { it.packageName }

        appItemList.value = pmRepo.getAppInfoList()
            .filter { !pmRepo.isSystemApp(it) }
            .map {
                AppItem(
                    it.packageName,
                    pmRepo.getLabel(it),
                    pmRepo.getIcon(it),
                    MutableLiveData(existingAppList.contains(it.packageName))
                )
            }
            .map { AppItemViewModel(it.packageName, it).toRecyclerItem() }
    }

    fun getCheckedAppList(): List<String> {
        return appItemList.value!!
            .map { it.viewModel }
            .filterIsInstance<AppItemViewModel>()
            .filter { it.appItem.checked.value == true }
            .map { it.appItem.packageName }
    }
}