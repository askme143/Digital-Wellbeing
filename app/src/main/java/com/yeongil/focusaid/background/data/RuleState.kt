package com.yeongil.focusaid.background.data

import com.yeongil.focusaid.data.rule.Rule
import kotlinx.serialization.Serializable

@Serializable
data class RuleState(
    val notified: Set<Rule> = setOf(),
    val conflicting: Set<Rule> = setOf(),
    val running: Set<Rule> = setOf(),
)