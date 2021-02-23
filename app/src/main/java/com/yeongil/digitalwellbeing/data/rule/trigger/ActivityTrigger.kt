package com.yeongil.digitalwellbeing.data.rule.trigger

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class ActivityTrigger(
    val activity: String
) : Parcelable