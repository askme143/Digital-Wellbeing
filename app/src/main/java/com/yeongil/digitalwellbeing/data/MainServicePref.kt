package com.yeongil.digitalwellbeing.data

import com.yeongil.digitalwellbeing.data.rule.Rule

data class MainServicePref(
    val timestamp: Long,
    val currentActivities: List<String>,
    val notifiedRules: List<Rule>,
    val conflictingRules: List<Rule>,
    val runningRules: List<Rule>,
) {
    constructor(timestamp: Long) : this(timestamp, listOf(), listOf(), listOf(), listOf())
}