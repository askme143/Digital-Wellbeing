package com.yeongil.digitalwellbeing.background

import android.app.Service
import android.content.Intent
import android.os.IBinder

class NotificationBlockService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent == null) return START_STICKY


        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}