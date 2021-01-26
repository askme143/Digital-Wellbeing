package com.yeongil.digitalwellbeing.viewModel.item

import android.graphics.drawable.Drawable
import com.yeongil.digitalwellbeing.repository.PackageManagerRepository

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