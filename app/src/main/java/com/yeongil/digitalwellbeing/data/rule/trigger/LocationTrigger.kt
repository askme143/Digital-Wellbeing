package com.yeongil.digitalwellbeing.data.rule.trigger

import kotlinx.serialization.Serializable

@Serializable
data class LocationTrigger(
    val latitude: Double,
    val longitude: Double,
    val range: Int,
    val locationName: String
)