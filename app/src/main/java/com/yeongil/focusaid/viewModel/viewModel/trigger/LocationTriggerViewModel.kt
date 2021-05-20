package com.yeongil.focusaid.viewModel.viewModel.trigger

import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.yeongil.focusaid.data.Location
import com.yeongil.focusaid.data.rule.trigger.LocationTrigger
import com.yeongil.focusaid.repository.LocationRepository
import com.yeongil.focusaid.utils.Event
import com.yeongil.focusaid.utils.FusedLocationClient
import com.yeongil.focusaid.utils.combineWith
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationTriggerViewModel(
    private val locationRepo: LocationRepository,
    private val fusedLocationClient: FusedLocationClient
) : ViewModel() {
    var noAnimation = Event(true)
        private set
    var fromSearchFragment = Event(false)
        private set

    val internetErrorEvent = MutableLiveData<Event<Unit>>()

    val progress = MutableLiveData(0)
    val latLng = MutableLiveData(LatLng(0.0, 0.0))
    val locationName = MutableLiveData("")

    val range = progress.map { it + 150 }
    val rangeText = range.map { "현재 범위: ${it}m" }

    val locationTrigger = latLng
        .combineWith(range) { latLng, range -> Pair(latLng, range) }
        .combineWith(locationName) { pair, locationName ->
            Triple(pair?.first, pair?.second, locationName)
        }
        .map { (latLng, range, locationName) ->
            if (latLng != null && range != null && locationName != null) {
                LocationTrigger(
                    latLng.latitude,
                    latLng.longitude,
                    range,
                    locationName
                )
            } else null
        }

    fun putLocationTrigger(trigger: LocationTrigger?) {
        noAnimation = Event(true)

        if (trigger != null) {
            progress.value = trigger.range - 150
            latLng.value = LatLng(trigger.latitude, trigger.longitude)
            locationName.value = trigger.locationName
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val location = fusedLocationClient.getLastLocation()

                withContext(Dispatchers.Main) {
                    progress.value = 0
                    if (location == null) {
                        latLng.value = LatLng(36.372237912297706, 127.36041371323982)
                        locationName.value = "한국과학기술원"
                    } else {
                        updateLatLng(LatLng(location.latitude, location.longitude))
                    }
                }
            }
        }
    }

    fun submitSearchResult(location: Location) {
        noAnimation = Event(true)

        latLng.value = location.latLng
        locationName.value = location.locationName
    }

    fun startSearch() {
        fromSearchFragment = Event(true)
    }

    fun updateLatLng(newLatLng: LatLng) {
        viewModelScope.launch(Dispatchers.IO) {
            locationRepo.latLngToLocationName(newLatLng).also { name ->
                withContext(Dispatchers.Main) {
                    if (name == null) {
                        internetErrorEvent.value = Event(Unit)
                    } else {
                        latLng.value = newLatLng
                        locationName.value = name
                    }
                }
            }
        }
    }
}