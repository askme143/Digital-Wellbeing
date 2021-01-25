package com.yeongil.digitalwellbeing.viewModel.item

import android.graphics.drawable.Drawable


data class AppBlockEntryItem(
    val packageName: String,
    val label: String,
    val icon: Drawable,
    val description: String,
)