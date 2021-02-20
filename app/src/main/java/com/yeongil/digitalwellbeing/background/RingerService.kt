package com.yeongil.digitalwellbeing.background

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.os.IBinder
import com.yeongil.digitalwellbeing.utils.RING
import com.yeongil.digitalwellbeing.utils.SILENT
import com.yeongil.digitalwellbeing.utils.VIBRATE

class RingerService : Service() {
    private val audioManager by lazy { getSystemService(AUDIO_SERVICE) as AudioManager }
    private val notificationManager by lazy { getSystemService(NOTIFICATION_SERVICE) as NotificationManager }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent == null) return START_STICKY
        val ringerExtra = intent.getIntExtra(RINGER_EXTRA_KEY, 0)
        val dndExtra = intent.getBooleanExtra(DND_EXTRA_KEY, false)

        /* DND First (Ringer mode can be affected by the change of dnd mode) */
        changeDndMode(dndExtra)
        changeRingerMode(ringerExtra)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun changeRingerMode(ringerMode: Int) {
        when (ringerMode) {
            VIBRATE -> {
                audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
            }
            RING -> {
                audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
            }
            SILENT -> {
                audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
            }
            else -> throw Exception("Invalid ringer mode")
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