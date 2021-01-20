package com.yeongil.digitalwellbeing.viewModelFactory

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeongil.digitalwellbeing.data.dao.rule.RuleDao
import com.yeongil.digitalwellbeing.repository.RuleRepository
import com.yeongil.digitalwellbeing.utils.SequenceNumber
import com.yeongil.digitalwellbeing.viewModel.RuleEditViewModel

@Suppress("UNCHECKED_CAST")
class RuleEditViewModelFactory(
    private val ruleDao: RuleDao,
    private val sharedPref: SharedPreferences
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RuleEditViewModel::class.java)) {
            RuleEditViewModel(RuleRepository(SequenceNumber(sharedPref), ruleDao)) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
