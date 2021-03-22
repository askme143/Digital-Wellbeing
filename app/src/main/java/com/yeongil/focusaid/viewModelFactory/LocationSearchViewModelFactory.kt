package com.yeongil.focusaid.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeongil.focusaid.dataSource.kakaoApi.KakaoApi
import com.yeongil.focusaid.repository.LocationRepository
import com.yeongil.focusaid.viewModel.viewModel.trigger.LocationSearchViewModel

@Suppress("UNCHECKED_CAST")
class LocationSearchViewModelFactory() : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LocationSearchViewModel::class.java)) {
            LocationSearchViewModel(LocationRepository(KakaoApi.service)) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}