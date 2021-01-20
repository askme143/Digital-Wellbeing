package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.yeongil.digitalwellbeing.data.dto.trigger.LocationTrigger
import com.yeongil.digitalwellbeing.utils.TEMPORAL_RID
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine

class LocationTriggerViewModel : ViewModel() {
    val progress = MutableLiveData<Int>(0)

    val latLng = MutableLiveData<LatLng>()
    private val range = liveData<Int> {
        progress.asFlow().collect { emit(it + 150) }
    }
    val rangeText = liveData<String> {
        range.asFlow().collect { emit("현재 범위: ${it}m") }
    }
    val locationTrigger = liveData<LocationTrigger?> {
        latLng.asFlow().combine(range.asFlow()) { latLng, range ->
            if (latLng != null) {
                LocationTrigger(
                    TEMPORAL_RID,
                    latLng.latitude,
                    latLng.longitude,
                    range,
                    ""
                )
            } else {
                null
            }
        }.collect { emit(it) }
    }
}