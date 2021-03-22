package com.yeongil.focusaid.data.rule

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.yeongil.focusaid.utils.TEMPORAL_RULE_ID
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class RuleInfo(
    val ruleId: Int = TEMPORAL_RULE_ID,
    @ColumnInfo(name = "rule_name") val ruleName: String = "",
    val activated: Boolean = true,
    @ColumnInfo(name = "noti_on_trigger") val notiOnTrigger: Boolean = false,
) : Parcelable