package com.yeongil.digitalwellbeing.viewModel.item

import androidx.lifecycle.MutableLiveData

data class KeywordItem(
    val id: String,
    val keyword: String,
    val inclusion: MutableLiveData<Boolean> = MutableLiveData(true),
)