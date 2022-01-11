package com.yeongil.focusaid.data.rule.action

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class AppBlockAction(
    val appBlockEntryList: List<AppBlockEntry>,
    val allAppBlock: Boolean,
    val allAppHandlingAction: Int,
) : Parcelable