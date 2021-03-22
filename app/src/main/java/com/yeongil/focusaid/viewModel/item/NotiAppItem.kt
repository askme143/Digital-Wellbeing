package com.yeongil.focusaid.viewModel.item

import android.graphics.drawable.Drawable
import com.yeongil.focusaid.repository.PackageManagerRepository

data class NotiAppItem(
    val packageName: String,
    val label: String,
    val icon: Drawable,
) {
    constructor(packageName: String, pmRepo: PackageManagerRepository) : this(
        packageName,
        pmRepo.getLabel(packageName),
        pmRepo.getIcon(packageName)
    )
}