package com.yeongil.digitalwellbeing.data.trigger

import kotlinx.serialization.Serializable

@Serializable
data class TimeTrigger(
    val startTimeInMinutes: Int,
    val endTimeInMinutes: Int,
    val repeatDay: List<Boolean>,
)