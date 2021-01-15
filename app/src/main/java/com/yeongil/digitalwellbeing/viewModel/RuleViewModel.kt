package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.data.dto.rule.Rule

class RuleViewModel : ViewModel() {
    private val _ruleList = MutableLiveData<List<Rule>>()
    val ruleList: LiveData<List<Rule>>
        get() = _ruleList

}