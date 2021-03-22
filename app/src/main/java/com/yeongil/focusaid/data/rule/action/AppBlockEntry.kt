package com.yeongil.focusaid.data.rule.action

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class AppBlockEntry(
    @ColumnInfo(name = "package_name") val packageName: String,
    @ColumnInfo(name = "allowed_time_in_minutes") val allowedTimeInMinutes: Int,
    @ColumnInfo(name = "handling_action") val handlingAction: Int,
) : Parcelable