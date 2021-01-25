package com.yeongil.digitalwellbeing.repository

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

class PackageManagerRepository(
    private val pm: PackageManager
) {
    fun getAppInfoList(): MutableList<ApplicationInfo> {
        return pm.getInstalledApplications(0)
    }

    fun getIcon(packageName: String): Drawable = pm.getApplicationIcon(packageName)
    fun getIcon(info: ApplicationInfo): Drawable = pm.getApplicationIcon(info)
    fun getLabel(info: ApplicationInfo): String = pm.getApplicationLabel(info).toString()
    fun getLabel(packageName: String): String = getLabel(pm.getApplicationInfo(packageName, 0))

    fun isSystemApp(info: ApplicationInfo): Boolean =
        (info.flags and ApplicationInfo.FLAG_SYSTEM) != 0
}