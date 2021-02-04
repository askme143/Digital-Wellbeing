package com.yeongil.digitalwellbeing.viewModel.viewModel.action

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.viewModel.item.NotiKeywordItem

class NotificationKeywordViewModel : ViewModel() {
    var id = ""
    var inclusion = true
    val keyword = MutableLiveData<String>()

    fun putNotiKeywordItem(item: NotiKeywordItem) {
        id = item.id
        inclusion = item.inclusion.value!!
        keyword.value = item.keyword
    }

    fun putNewNotiKeywordItem() {
        id = System.currentTimeMillis().toString()
        inclusion = true
        keyword.value = ""
    }

    fun getNotiKeywordItem() = NotiKeywordItem(id, keyword.value!!, MutableLiveData(inclusion))
}