package com.yeongil.focusaid.viewModel.item

import androidx.lifecycle.MutableLiveData

data class NotiKeywordItem(
    val id: String,
    val keyword: String,
    val inclusion: MutableLiveData<Boolean> = MutableLiveData(true),
)