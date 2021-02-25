package com.yeongil.digitalwellbeing.background

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.os.IBinder
import com.yeongil.digitalwellbeing.data.rule.action.RingerAction.RingerMode

class RingerService : Service() {
    private val audioManager by lazy { getSystemService(AUDIO_SERVICE) as AudioManager }
    private val notificationManager by lazy { getSystemService(NOTIFICATION_SERVICE) as NotificationManager }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent == null) return START_STICKY
        val ringerMode = intent.getParcelableExtra(RINGER_EXTRA_KEY) ?: RingerMode.VIBRATE
        val dndExtra = intent.getBooleanExtra(DND_EXTRA_KEY, false)

        /* DND First (Ringer mode can be affected by the change of dnd mode) */
        changeDndMode(dndExtra)
        changeRingerMode(ringerMode)

        stopSelf(startId)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun changeRingerMode(ringerMode: RingerMode) {
        when (ringerMode) {
            RingerMode.VIBRATE -> {
                audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
            }
            RingerMode.RING -> {
                audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
            }
            RingerMode.SILENT -> {
                audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
            }
        }
    }

    private fun changeDndMode(turnOn: Boolean) {
        if (turnOn) {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
        } else {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
        }
    }

    companion object {
        const val RINGER_EXTRA_KEY = "RINGER_EXTRA"
        const val DND_EXTRA_KEY = "DND_EXTRA"
    }
}