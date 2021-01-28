package com.yeongil.digitalwellbeing.data

import com.google.android.gms.maps.model.LatLng

data class Location(
    val id: String,
    val locationName: String,
    val address: String,
    val latLng: LatLng
)