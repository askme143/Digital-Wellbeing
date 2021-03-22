package com.yeongil.focusaid.data.rule.action

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class DndAction(
    val temp: Boolean = true
) : Parcelable