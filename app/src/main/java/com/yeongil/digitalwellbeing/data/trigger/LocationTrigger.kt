package com.yeongil.digitalwellbeing.data.trigger

import kotlinx.serialization.Serializable

@Serializable
data class LocationTrigger(
    val latitude: Double,
    val longitude: Double,
    val range: Int,
    val locationName: String
)