package com.yeongil.focusaid.viewModelFactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.yeongil.focusaid.dataSource.kakaoApi.KakaoApi
import com.yeongil.focusaid.repository.LocationRepository
import com.yeongil.focusaid.utils.FusedLocationClient
import com.yeongil.focusaid.viewModel.viewModel.trigger.LocationSearchViewModel

@Suppress("UNCHECKED_CAST")
class LocationSearchViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LocationSearchViewModel::class.java)) {
            LocationSearchViewModel(
                LocationRepository(KakaoApi.service),
                FusedLocationClient(LocationServices.getFusedLocationProviderClient(context.applicationContext))
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}