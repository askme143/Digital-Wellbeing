package com.yeongil.focusaid.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeongil.focusaid.repository.PackageManagerRepository
import com.yeongil.focusaid.viewModel.viewModel.AppBlockViewModel

@Suppress("UNCHECKED_CAST")
class AppBlockViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AppBlockViewModel::class.java)) {
            AppBlockViewModel(PackageManagerRepository(context.applicationContext.packageManager)) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}