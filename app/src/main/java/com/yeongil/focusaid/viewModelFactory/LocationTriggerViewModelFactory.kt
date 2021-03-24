package com.yeongil.focusaid.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.yeongil.focusaid.dataSource.kakaoApi.KakaoApi
import com.yeongil.focusaid.repository.LocationRepository
import com.yeongil.focusaid.utils.FusedLocationClient
import com.yeongil.focusaid.viewModel.viewModel.trigger.LocationTriggerViewModel

@Suppress("UNCHECKED_CAST")
class LocationTriggerViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LocationTriggerViewModel::class.java)) {
            LocationTriggerViewModel(
                LocationRepository(KakaoApi.service),
                FusedLocationClient(LocationServices.getFusedLocationProviderClient(context.applicationContext))
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}