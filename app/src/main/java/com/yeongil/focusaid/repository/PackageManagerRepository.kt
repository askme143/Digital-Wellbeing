package com.yeongil.focusaid.repository

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

class PackageManagerRepository(
    private val pm: PackageManager,
) {
//    private val exceptionList =

    @SuppressLint("QueryPermissionsNeeded")
    fun getAppInfoList(): MutableList<ApplicationInfo> {
        return pm.getInstalledApplications(0)
    }

    fun getIcon(packageName: String): Drawable = pm.getApplicationIcon(packageName)
    fun getIcon(info: ApplicationInfo): Drawable = pm.getApplicationIcon(info)
    fun getLabel(info: ApplicationInfo): String = pm.getApplicationLabel(info).toString()
    fun getLabel(packageName: String): String = getLabel(pm.getApplicationInfo(packageName, 0))

    fun isSystemApp(info: ApplicationInfo): Boolean =
        (info.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                && info.packageName != "com.google.android.youtube"
                && info.packageName != "com.google.android.gm"
                || info.packageName == "com.yeongil.focusaid"
                || info.packageName == "com.samsung.android.spay"
                || info.packageName == "com.samsung.android.spayfw"

    fun isSystemApp(packageName: String): Boolean =
        isSystemApp(pm.getApplicationInfo(packageName, 0))
}