package com.yeongil.digitalwellbeing

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class DigitalWellbeingApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}