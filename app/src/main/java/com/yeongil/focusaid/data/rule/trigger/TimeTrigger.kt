package com.yeongil.focusaid.data.rule.trigger

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class TimeTrigger(
    val startTimeInMinutes: Int,
    val endTimeInMinutes: Int,
    val repeatDay: List<Boolean>,
) : Parcelable