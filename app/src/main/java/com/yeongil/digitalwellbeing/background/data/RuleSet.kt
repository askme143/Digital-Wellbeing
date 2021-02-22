package com.yeongil.digitalwellbeing.background.data

import com.yeongil.digitalwellbeing.data.rule.Rule
import kotlinx.serialization.Serializable

@Serializable
data class RuleSet(
    val notified: List<Rule> = listOf(),
    val conflicting: List<Rule> = listOf(),
    val running: List<Rule> = listOf(),
)