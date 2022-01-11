package com.yeongil.focusaid.data.rule.action

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class AppBlockEntry(
    val packageName: String,
    val allowedTimeInMinutes: Int,
    val handlingAction: Int,
) : Parcelable