package com.yeongil.focusaid.background

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.IBinder
import androidx.core.content.edit
import com.yeongil.focusaid.data.rule.action.RingerAction.RingerMode


class RingerService : Service() {
    private val audioManager by lazy { getSystemService(AUDIO_SERVICE) as AudioManager }
    private val notificationManager by lazy { getSystemService(NOTIFICATION_SERVICE) as NotificationManager }
    private val sharedPref by lazy {
        getSharedPreferences("com.yeongil.digitalwellbeing.Ringer_Service", Context.MODE_PRIVATE)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent == null) return START_STICKY

        when (intent.action) {
            RINGER_MODE_CHANGE_DETECTED -> {
                if (isRingerChangeEpoch())
                    endRingerChangeEpoch()
                else
                    updateDefaultRingerMode()
            }
            RUN_ACTION -> {
                val ringerMode =
                    intent.getParcelableExtra(RINGER_EXTRA_KEY) ?: getDefaultRingerMode()
                val dndExtra = intent.getBooleanExtra(DND_EXTRA_KEY, false)

                /* DND First (Ringer mode can be affected by the change of dnd mode) */
                changeDndMode(dndExtra)
                changeRingerMode(ringerMode)
            }
        }

        stopSelf(startId)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun changeRingerMode(ringerMode: RingerMode) {
        updateDefaultRingerMode()
        startRingerChangeEpoch()

        when (ringerMode) {
            RingerMode.VIBRATE -> audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
            RingerMode.RING -> audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
            RingerMode.SILENT -> audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
        }
    }

    private fun changeDndMode(turnOn: Boolean) {
        if (turnOn) {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE)
        } else {
            notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL)
        }
    }

    private fun isRingerChangeEpoch(): Boolean {
        return sharedPref.getBoolean(RINGER_CHANGE_EPOCH_KEY, false)
    }

    private fun startRingerChangeEpoch() {
        sharedPref.edit {
            putBoolean(RINGER_CHANGE_EPOCH_KEY, true)
            commit()
        }
    }

    private fun endRingerChangeEpoch() {
        sharedPref.edit {
            putBoolean(RINGER_CHANGE_EPOCH_KEY, false)
            commit()
        }
    }

    private fun updateDefaultRingerMode() {
        val ringerMode = when (audioManager.ringerMode) {
            AudioManager.RINGER_MODE_VIBRATE -> RingerMode.VIBRATE
            AudioManager.RINGER_MODE_NORMAL -> RingerMode.RING
            AudioManager.RINGER_MODE_SILENT -> RingerMode.SILENT
            else -> RingerMode.VIBRATE
        }

        sharedPref.edit {
            putInt(DEFAULT_RINGER_MODE_KEY, ringerMode.ordinal)
            commit()
        }
    }

    private fun getDefaultRingerMode(): RingerMode {
        val idx = sharedPref.getInt(DEFAULT_RINGER_MODE_KEY, 0)
        return enumValues<RingerMode>()[idx]
    }

    companion object {
        const val RINGER_MODE_CHANGE_DETECTED = "RINGER_MODE_CHANGE_DETECTED"
        const val RUN_ACTION = "RUN_ACTION"

        const val RINGER_EXTRA_KEY = "RINGER_EXTRA"
        const val DND_EXTRA_KEY = "DND_EXTRA"

        const val DEFAULT_RINGER_MODE_KEY = "DEFAULT_RINGER_MODE"
        const val RINGER_CHANGE_EPOCH_KEY = "RINGER_CHANGE_EPOCH"
    }
}