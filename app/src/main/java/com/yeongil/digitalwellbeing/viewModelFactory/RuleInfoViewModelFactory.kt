package com.yeongil.digitalwellbeing.viewModelFactory

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yeongil.digitalwellbeing.dataSource.SequenceNumber
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.RuleDatabase
import com.yeongil.digitalwellbeing.repository.RuleRepository
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.RuleInfoViewModel

@Suppress("UNCHECKED_CAST")
class RuleInfoViewModelFactory(private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(RuleInfoViewModel::class.java)) {
            RuleInfoViewModel(
                RuleRepository(
                    SequenceNumber(context.applicationContext),
                    RuleDatabase.getInstance(context.applicationContext).ruleDao()
                )
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}