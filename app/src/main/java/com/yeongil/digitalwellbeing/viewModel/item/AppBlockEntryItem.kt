package com.yeongil.digitalwellbeing.viewModel.item

import android.graphics.drawable.Drawable
import com.yeongil.digitalwellbeing.data.rule.action.AppBlockEntry
import com.yeongil.digitalwellbeing.repository.PackageManagerRepository
import com.yeongil.digitalwellbeing.utils.ALERT
import com.yeongil.digitalwellbeing.utils.CLOSE_IMMEDIATE
import com.yeongil.digitalwellbeing.utils.TimeUtils


data class AppBlockEntryItem(
    val packageName: String,
    val label: String,
    val icon: Drawable,
    val description: String,
) {
    constructor(entry: AppBlockEntry, pmRepo: PackageManagerRepository) : this(
        entry.packageName,
        pmRepo.getLabel(entry.packageName),
        pmRepo.getIcon(entry.packageName),
        entry.allowedTimeInMinutes.let {
            val action = when (entry.handlingAction) {
                CLOSE_IMMEDIATE -> "바로 종료"
                ALERT -> "경고 알림"
                else -> ""
            }

            if (it == 0) "실행 시 $action"
            else "${TimeUtils.minutesToTimeMinute(it)} 후 $action"
        }
    )
}