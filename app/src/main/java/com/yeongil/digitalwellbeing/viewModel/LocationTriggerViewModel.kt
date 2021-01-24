package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.liveData
import com.google.android.gms.maps.model.LatLng
import com.yeongil.digitalwellbeing.data.trigger.LocationTrigger
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine

class LocationTriggerViewModel : ViewModel() {
    val progress = MutableLiveData(0)

    val latLng = MutableLiveData<LatLng>()

    private val range = liveData {
        progress.asFlow().collect { emit(it + 150) }
    }
    val rangeText = liveData {
        range.asFlow().collect { emit("현재 범위: ${it}m") }
    }

    val locationTrigger = liveData {
        latLng.asFlow().combine(range.asFlow()) { latLng, range ->
            if (latLng != null) {
                LocationTrigger(
                    latLng.latitude,
                    latLng.longitude,
                    range,
                    latLng.toString() + range.toString()
                )
            } else {
                null
            }
        }.collect { emit(it) }
    }

    fun init(newLatLng: LatLng) {
        progress.value = 0
        latLng.value = newLatLng
    }

    fun init(locationTrigger: LocationTrigger) {
        progress.value = locationTrigger.range - 150
        latLng.value = LatLng(locationTrigger.latitude, locationTrigger.longitude)
    }
}