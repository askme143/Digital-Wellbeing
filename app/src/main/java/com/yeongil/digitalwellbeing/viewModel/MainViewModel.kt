package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.*
import com.yeongil.digitalwellbeing.database.ruleDatabase.dao.rule.RuleDao
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.RuleInfoItemViewModel
import kotlinx.coroutines.flow.collect

class MainViewModel(
    private val ruleDao: RuleDao
) : ViewModel() {
    val ruleInfoItemList: LiveData<List<RecyclerItem>> = liveData {
        ruleDao.getRuleInfoListFlow().collect {
            it.map { ruleInfo ->
                RuleInfoItemViewModel(
                    ruleInfo.rid.toString(),
                    ruleInfo,
                    ruleDao,
                    viewModelScope
                ).toRecyclerItem()
            }.also { recyclerItems -> emit(recyclerItems) }
        }
    }
}