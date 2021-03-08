package com.yeongil.digitalwellbeing.viewModel.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.yeongil.digitalwellbeing.repository.PackageManagerRepository

class AppBlockViewModel(
    private val pmRepo: PackageManagerRepository
) : ViewModel() {
    var blockingAppKey = ""
        private set

    private val packageName = MutableLiveData<String>()
    val appLabel = packageName.map { pmRepo.getLabel(it) }

    val isClose = MutableLiveData<Boolean>()

    fun putPackageName(packageName: String) {
        this.packageName.value = packageName
    }

    fun putBlockingAppKey(blockingAppKey: String) {
        this.blockingAppKey = blockingAppKey
    }

    fun setClose() = run { isClose.value = true }
    fun setAlert() = run { isClose.value = false }
}