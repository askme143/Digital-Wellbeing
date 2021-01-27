package com.yeongil.digitalwellbeing.viewModel.viewModel.rule

import androidx.lifecycle.*
import com.yeongil.digitalwellbeing.data.rule.Rule
import com.yeongil.digitalwellbeing.data.rule.RuleInfo
import com.yeongil.digitalwellbeing.repository.RuleRepository
import com.yeongil.digitalwellbeing.utils.Event
import com.yeongil.digitalwellbeing.utils.recyclerViewUtils.RecyclerItem
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.RuleInfoItemViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RuleInfoViewModel(
    private val ruleRepo: RuleRepository
) : ViewModel() {
    val ruleInfoItemList: LiveData<List<RecyclerItem>> = liveData {
        ruleRepo.getRuleInfoListFlow().collect {
            it.map { ruleInfo ->
                RuleInfoItemViewModel(
                    ruleInfo.ruleId.toString(),
                    ruleInfo,
                    onClickActivate,
                    onClickNotiOnTrigger,
                    onClickDelete,
                    onClickItem
                ).toRecyclerItem()
            }.also { recyclerItems -> emit(recyclerItems) }
        }
    }
    val itemClickEvent = MutableLiveData<Event<Int>>()

    private val onClickActivate: (RuleInfo) -> Unit = {
        val newRuleInfo = it.copy(activated = !it.activated)
        viewModelScope.launch { ruleRepo.updateRuleInfo(newRuleInfo) }
    }
    private val onClickNotiOnTrigger: (RuleInfo) -> Unit = {
        val newRuleInfo = it.copy(notiOnTrigger = !it.notiOnTrigger)
        viewModelScope.launch { ruleRepo.updateRuleInfo(newRuleInfo) }
    }
    private val onClickDelete: (Int) -> Unit = { ruleId ->
        viewModelScope.launch { ruleRepo.ruleDao.deleteRuleByRid(ruleId) }
    }
    private val onClickItem: (Int) -> Unit = { ruleId ->
        itemClickEvent.value = Event(ruleId)
    }
}