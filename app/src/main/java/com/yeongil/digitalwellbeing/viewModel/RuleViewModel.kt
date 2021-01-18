package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.*
import com.yeongil.digitalwellbeing.data.dao.RuleDao
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.RuleInfoItemViewModel
import kotlinx.coroutines.flow.collect

class RuleViewModel(
    private val ruleDao: RuleDao
) : ViewModel() {
    val ruleInfoItemList: LiveData<List<RecyclerItem>> = liveData {
        ruleDao.getRuleInfoListFlow().collect {
            it.map { ruleInfo ->
                RuleInfoItemViewModel(
                    ruleInfo,
                    ruleDao,
                    viewModelScope
                ).toRecyclerItem()
            }
                .also { recyclerItems -> emit(recyclerItems) }
        }
    }
}