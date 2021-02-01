package com.yeongil.digitalwellbeing

import android.app.*
import android.content.Intent
import android.os.IBinder
import com.yeongil.digitalwellbeing.background.MainNotification

class MainService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = MainNotification.createNotification(this)
        startForeground(1, notification)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }
}