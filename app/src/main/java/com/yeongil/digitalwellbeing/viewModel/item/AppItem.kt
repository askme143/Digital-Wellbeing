package com.yeongil.digitalwellbeing.viewModel.item

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.lifecycle.MutableLiveData

data class AppItem(
    val packageName: String,
    val label: String,
    val icon: Drawable,
    val checked: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
)