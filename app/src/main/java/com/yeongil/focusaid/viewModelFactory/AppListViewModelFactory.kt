package com.yeongil.focusaid.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeongil.focusaid.repository.PackageManagerRepository
import com.yeongil.focusaid.viewModel.viewModel.action.AppListViewModel

@Suppress("UNCHECKED_CAST")
class AppListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AppListViewModel::class.java)) {
            AppListViewModel(PackageManagerRepository(context.applicationContext.packageManager)) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}