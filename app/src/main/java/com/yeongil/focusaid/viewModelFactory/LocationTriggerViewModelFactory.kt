package com.yeongil.focusaid.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeongil.focusaid.dataSource.kakaoApi.KakaoApi
import com.yeongil.focusaid.repository.LocationRepository
import com.yeongil.focusaid.viewModel.viewModel.trigger.LocationTriggerViewModel

@Suppress("UNCHECKED_CAST")
class LocationTriggerViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LocationTriggerViewModel::class.java)) {
            LocationTriggerViewModel(LocationRepository(KakaoApi.service)) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}