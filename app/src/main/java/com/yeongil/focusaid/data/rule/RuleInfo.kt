package com.yeongil.focusaid.data.rule

import android.os.Parcelable
import com.yeongil.focusaid.utils.TEMPORAL_RULE_ID
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class RuleInfo(
    val ruleId: Int = TEMPORAL_RULE_ID,
    val ruleName: String = "",
    val activated: Boolean = true,
    val notiOnTrigger: Boolean = false,
) : Parcelable