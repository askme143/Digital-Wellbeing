package com.yeongil.digitalwellbeing.data.rule.trigger

import kotlinx.serialization.Serializable

@Serializable
data class ActivityTrigger(
    val activity: String
)