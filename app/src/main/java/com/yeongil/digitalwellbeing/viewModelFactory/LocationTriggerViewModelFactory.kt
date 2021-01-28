package com.yeongil.digitalwellbeing.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeongil.digitalwellbeing.dataSource.kakaoApi.KakaoApi
import com.yeongil.digitalwellbeing.repository.LocationRepository
import com.yeongil.digitalwellbeing.viewModel.viewModel.trigger.LocationTriggerViewModel

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