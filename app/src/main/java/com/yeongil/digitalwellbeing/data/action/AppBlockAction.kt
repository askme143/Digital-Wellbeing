package com.yeongil.digitalwellbeing.data.action

import kotlinx.serialization.Serializable

@Serializable
data class AppBlockAction(
    val appBlockEntryList: List<AppBlockEntry>,
    val handlingAction: Int,    //TODO: handling_action : integer mapping table
)