package com.yeongil.focusaid.viewModel.viewModel.trigger

import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.yeongil.focusaid.data.Location
import com.yeongil.focusaid.repository.LocationRepository
import com.yeongil.focusaid.utils.Event
import com.yeongil.focusaid.utils.FusedLocationClient
import com.yeongil.focusaid.viewModel.itemViewModel.LocationItemViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocationSearchViewModel(
    private val locationRepo: LocationRepository,
    private val fusedLocationClient: FusedLocationClient
) : ViewModel() {
    private var latLng: LatLng? = null
    private var locationBasedSearch: Boolean = true

    val keyword = MutableLiveData<String>()
    val searchErrorEvent = MutableLiveData<Event<Unit>>()
    val currLocationErrorEvent = MutableLiveData<Event<Unit>>()

    private val locationList = MutableLiveData<List<Location>>()
    val locationRecyclerItemList = liveData {
        locationList.asFlow().collect { list ->
            emit(
                list.map { LocationItemViewModel(it.id, it, onClickItem) }
                    .map { it.toRecyclerItem() }
            )
        }
    }
    val itemClickEvent = MutableLiveData<Event<Location>>()
    val onClickItem: (String) -> Unit = { id ->
        val selectedItem = locationList.value!!.last { it.id == id }
        keyword.value = selectedItem.locationName
        itemClickEvent.value = Event(selectedItem)
    }

    fun search() {
        val keyword = this.keyword.value ?: ""

        viewModelScope.launch(Dispatchers.IO) {
            Log.e("hello", locationBasedSearch.toString())
            locationRepo.getKeywordLocationList(keyword, latLng, locationBasedSearch)
                .also { list ->
                    launch(Dispatchers.Main) {
                        if (list == null) {
                            locationList.value = emptyList()
                            searchErrorEvent.value = Event(Unit)
                        } else {
                            locationList.value = list
                        }
                    }
                }
        }
    }

    fun setKeyword(keyword: String) {
        this.keyword.value = keyword
        locationList.value = listOf()
    }

    fun updateCurrentLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            val location = fusedLocationClient.getLastLocation()

            if (location != null) {
                latLng = LatLng(location.latitude, location.longitude)
                locationBasedSearch = true
            } else {
                withContext(Dispatchers.Main) {
                    latLng = null
                    locationBasedSearch = false
                    currLocationErrorEvent.value = Event(Unit)
                }
            }
        }
    }
}