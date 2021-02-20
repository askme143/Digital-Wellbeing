package com.yeongil.digitalwellbeing.data.rule.action

import kotlinx.serialization.Serializable

@Serializable
data class DndAction(
    val temp: Boolean = true
)