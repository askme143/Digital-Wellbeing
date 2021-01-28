package com.yeongil.digitalwellbeing.viewModel.viewModel.trigger

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.yeongil.digitalwellbeing.data.Location
import com.yeongil.digitalwellbeing.data.trigger.LocationTrigger
import com.yeongil.digitalwellbeing.repository.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class LocationTriggerViewModel(
    private val locationRepo: LocationRepository
) : ViewModel() {
    var editing = false

    val progress = MutableLiveData(0)
    val latLng = MutableLiveData<LatLng>()
    val locationName = MutableLiveData<String>()

    private val range = liveData {
        progress.asFlow().collect { emit(it + 150) }
    }
    val rangeText = liveData {
        range.asFlow().collect { emit("현재 범위: ${it}m") }
    }

    val locationTrigger = liveData {
        latLng.asFlow()
            .combine(range.asFlow()) { latLng, range -> Pair(latLng, range) }
            .combine(locationName.asFlow()) { (latLng, range), locationName ->
                if (latLng != null) {
                    LocationTrigger(
                        latLng.latitude,
                        latLng.longitude,
                        range,
                        locationName
                    )
                } else null
            }
            .collect { emit(it) }
    }

    fun init(newLatLng: LatLng) {
        progress.value = 0
        latLng.value = newLatLng
        locationName.value = ""
        doReverseGeocoding()
    }

    fun init(locationTrigger: LocationTrigger) {
        progress.value = locationTrigger.range - 150
        latLng.value = LatLng(locationTrigger.latitude, locationTrigger.longitude)
        locationName.value = locationTrigger.locationName
    }

    fun submitSearchResult(location: Location) {
        latLng.value = location.latLng
        locationName.value = location.locationName
    }

    fun doReverseGeocoding() {
        viewModelScope.launch(Dispatchers.IO) {
            locationRepo.latLngToLocationName(latLng.value!!)
                .also { launch(Dispatchers.Main) { locationName.value = it ?: "Error" } }
        }
    }
}