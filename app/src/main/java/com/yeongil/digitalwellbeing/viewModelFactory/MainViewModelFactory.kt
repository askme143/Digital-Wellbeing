package com.yeongil.digitalwellbeing.viewModelFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeongil.digitalwellbeing.database.ruleDatabase.dao.rule.RuleDao
import com.yeongil.digitalwellbeing.viewModel.MainViewModel

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val ruleDao: RuleDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            MainViewModel(ruleDao) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}