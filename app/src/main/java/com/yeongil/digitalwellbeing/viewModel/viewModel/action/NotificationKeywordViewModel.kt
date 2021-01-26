package com.yeongil.digitalwellbeing.viewModel.viewModel.action

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.viewModel.item.NotiKeywordItem

class NotificationKeywordViewModel : ViewModel() {
    var id = ""
    var inclusion = true
    val keyword = MutableLiveData<String>()

    fun init() {
        id = System.currentTimeMillis().toString()
        inclusion = true
        keyword.value = ""
    }

    fun init(keywordItem: NotiKeywordItem) {
        id = keywordItem.id
        inclusion = keywordItem.inclusion.value!!
        keyword.value = keywordItem.keyword
    }

    fun getKeywordItem() = NotiKeywordItem(id, keyword.value!!, MutableLiveData(inclusion))
}