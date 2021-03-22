package com.yeongil.focusaid.viewModel.item

import android.graphics.drawable.Drawable
import androidx.lifecycle.MutableLiveData

data class AppItem(
    val packageName: String,
    val label: String,
    val icon: Drawable,
    val checked: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
)