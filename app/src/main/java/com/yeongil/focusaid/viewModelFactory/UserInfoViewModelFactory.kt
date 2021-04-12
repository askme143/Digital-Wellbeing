package com.yeongil.focusaid.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeongil.focusaid.dataSource.user.UserInfoPref
import com.yeongil.focusaid.viewModel.viewModel.UserInfoViewModel

@Suppress("UNCHECKED_CAST")
class UserInfoViewModelFactory(private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(UserInfoViewModel::class.java)) {
            UserInfoViewModel(
                UserInfoPref(context.applicationContext)
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}