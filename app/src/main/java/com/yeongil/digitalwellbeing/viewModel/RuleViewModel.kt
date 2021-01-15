package com.yeongil.digitalwellbeing.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.data.dao.RuleDao
import com.yeongil.digitalwellbeing.data.dto.rule.Rule
import com.yeongil.digitalwellbeing.data.dto.rule.RuleInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RuleViewModel(
    private val ruleDao: RuleDao
) : ViewModel() {
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    private var ruleList: List<Rule> = listOf()

    private val _ruleInfoList = MutableLiveData<List<RuleInfo>>()
    val ruleInfoList: LiveData<List<RuleInfo>>
        get() = _ruleInfoList

    fun loadRuleList() {
        coroutineScope.launch {
            ruleList = ruleDao.getRuleList()

            val loadedRuleInfoList = ruleList.map { it.ruleInfo }

            launch(
                Dispatchers.Main
            ) {
                _ruleInfoList.value = loadedRuleInfoList
            }
        }
    }
}