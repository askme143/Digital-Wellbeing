package com.yeongil.focusaid.background.data

import com.yeongil.focusaid.data.rule.Rule
import kotlinx.serialization.Serializable

@Serializable
data class RuleSet(
    val notified: List<Rule> = listOf(),
    val conflicting: List<Rule> = listOf(),
    val running: List<Rule> = listOf(),
)