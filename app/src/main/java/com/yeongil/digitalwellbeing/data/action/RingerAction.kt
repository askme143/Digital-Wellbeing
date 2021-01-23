package com.yeongil.digitalwellbeing.data.action

import kotlinx.serialization.Serializable

@Serializable
data class RingerAction(
    val rid: Int,
    val ringerMode: Int,
)