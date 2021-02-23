package com.yeongil.digitalwellbeing.data.rule.trigger

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class LocationTrigger(
    val latitude: Double,
    val longitude: Double,
    val range: Int,
    val locationName: String
) : Parcelable