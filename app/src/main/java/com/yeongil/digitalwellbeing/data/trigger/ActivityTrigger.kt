package com.yeongil.digitalwellbeing.data.trigger

import kotlinx.serialization.Serializable

@Serializable
data class ActivityTrigger(
    val activity: String
)