package com.yeongil.digitalwellbeing.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeongil.digitalwellbeing.dataSource.kakaoApi.KakaoApi
import com.yeongil.digitalwellbeing.repository.LocationRepository
import com.yeongil.digitalwellbeing.viewModel.viewModel.trigger.LocationSearchViewModel
import kotlinx.serialization.ExperimentalSerializationApi

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