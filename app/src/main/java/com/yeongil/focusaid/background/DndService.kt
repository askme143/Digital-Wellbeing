package com.yeongil.focusaid.background

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.yeongil.focusaid.BuildConfig


class DndService : Service() {
    private val notificationManager by lazy { getSystemService(NOTIFICATION_SERVICE) as NotificationManager }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent == null) return START_STICKY

        when (intent.action) {
            RUN_ACTION -> {
                val dndExtra = intent.getBooleanExtra(DND_EXTRA_KEY, false)
                changeDndMode(dndExtra)
            }
        }

        stopSelf(startId)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun changeDndMode(turnOn: Boolean) {
        if (turnOn) {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
        } else {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
        }
    }

    companion object {
        const val RUN_ACTION = "${BuildConfig.APPLICATION_ID}.RUN_ACTION"
        const val DND_EXTRA_KEY = "DND_EXTRA"
    }
}