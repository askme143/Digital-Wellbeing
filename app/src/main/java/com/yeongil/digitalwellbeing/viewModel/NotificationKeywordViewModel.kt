package com.yeongil.digitalwellbeing.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yeongil.digitalwellbeing.viewModel.item.KeywordItem

class NotificationKeywordViewModel : ViewModel() {
    var id = ""
    var inclusion = true
    val keyword = MutableLiveData<String>()

    fun init() {
        id = System.currentTimeMillis().toString()
        inclusion = true
        keyword.value = ""
    }

    fun init(keywordItem: KeywordItem) {
        id = keywordItem.id
        inclusion = keywordItem.inclusion.value!!
        keyword.value = keywordItem.keyword
    }

    fun getKeywordItem() = KeywordItem(id, keyword.value!!, MutableLiveData(inclusion))
}