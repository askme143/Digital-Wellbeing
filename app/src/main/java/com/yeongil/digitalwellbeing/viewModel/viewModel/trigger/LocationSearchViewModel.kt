package com.yeongil.digitalwellbeing.viewModel.viewModel.trigger

import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Tasks.await
import com.yeongil.digitalwellbeing.data.Location
import com.yeongil.digitalwellbeing.repository.LocationRepository
import com.yeongil.digitalwellbeing.utils.Event
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.LocationItemViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class LocationSearchViewModel(
    private val locationRepo: LocationRepository
) : ViewModel() {
    var latLng: LatLng? = null

    val keyword = MutableLiveData<String>()

    private val locationList = MutableLiveData<List<Location>>()
    val locationRecyclerItemList = liveData<List<RecyclerItem>> {
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
            locationRepo.getKeywordLocationList(keyword, latLng)
                .also {
                    launch(Dispatchers.Main) { locationList.value = it }
                }
        }
    }

    fun setKeyword(keyword: String) {
        this.keyword.value = keyword
        locationList.value = listOf()
    }

    fun init(latLng: LatLng?) {
        this.latLng = latLng
    }
}