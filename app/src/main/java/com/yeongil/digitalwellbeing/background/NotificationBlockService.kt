package com.yeongil.digitalwellbeing.background

import android.app.Notification
import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.*
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.edit
import com.yeongil.digitalwellbeing.data.rule.action.NotificationAction
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class NotificationBlockService : NotificationListenerService() {
    private val sharedPref by lazy {
        getSharedPreferences(NOTI_BLOCK_SERVICE_PREF_NAME, MODE_PRIVATE)
    }
    private val vibrator by lazy { getSystemService(VIBRATOR_SERVICE) as Vibrator }
    private val notificationManager by lazy { getSystemService(NOTIFICATION_SERVICE) as NotificationManager }
    private val userManager by lazy { getSystemService(USER_SERVICE) as UserManager }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent == null) return START_STICKY
        val action = intent.getParcelableExtra<NotificationAction?>(NOTIFICATION_EXTRA_KEY)
        sharedPref.edit {
            putString(NOTIFICATION_ACTION_KEY, Json.encodeToString(action))
            putLong(TIMESTAMP_NOTIFICATION_ACTION_KEY, System.currentTimeMillis())
            commit()
        }

        Log.e("hello", "Notification Action Changed: ${action.toString()}")

        return START_STICKY
    }

    private fun getNotificationAction(): NotificationAction? {
        val defaultStr by lazy { Json.encodeToString(null as NotificationAction?) }

        return sharedPref.getString(NOTIFICATION_ACTION_KEY, defaultStr)
            ?.let { Json.decodeFromString(it) }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        if (sbn == null) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && sbn in snoozedNotifications) {
            Log.e("hello", "snoozed?")
            return
        }
        val action = getNotificationAction() ?: return

        val pkgName by lazy { sbn.packageName }

        val extra by lazy { sbn.notification.extras }
        val title by lazy { extra.getString(Notification.EXTRA_TITLE) ?: "" }
        val text by lazy { extra.getString(Notification.EXTRA_TEXT) ?: "" }
        val subtext by lazy { extra.getString(Notification.EXTRA_SUB_TEXT) ?: "" }
        val message by lazy { extra.getString(Notification.EXTRA_TEXT_LINES) ?: "" }

        if (action.allApp || pkgName in action.appList) {
            if (action.keywordList.any {
                    if (it.inclusion) {
                        it.keyword in title || it.keyword in text || it.keyword in subtext || it.keyword in message
                    } else {
                        it.keyword !in title || it.keyword !in text || it.keyword !in subtext || it.keyword !in message
                    }
                }) {
                /* TODO: Debug */
                when (action.handlingAction) {
                    0 -> cancelNotification(sbn.key) // HIDE
                    1 -> {
                        // VIBRATE
                        sbn.notification.priority = NotificationManagerCompat.IMPORTANCE_LOW
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(
                                VibrationEffect.createOneShot(
                                    250,
                                    VibrationEffect.DEFAULT_AMPLITUDE
                                )
                            )
                        } else {
                            vibrator.vibrate(250)
                        }
                    }
                    2 -> {
                        // RING
                        sbn.notification.priority = NotificationManagerCompat.IMPORTANCE_LOW
                        val ringtoneUri =
                            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
                                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                        RingtoneManager.getRingtone(this, ringtoneUri).play()
                    }
                    3 -> {
                        // SILENT
                        sbn.notification.priority = Notification.PRIORITY_MIN
                        sbn.notification.vibrate = longArrayOf(0)
                        sbn.notification.audioAttributes = null
                    }
                }
            }
        }
    }

    companion object {
        const val NOTI_BLOCK_SERVICE_PREF_NAME =
            "com.yeongil.digitalwellbeing.NOTIFICATION_BLOCK_SERVICE"

        const val TIMESTAMP_NOTIFICATION_ACTION_KEY = "TIMESTAMP_NOTIFICATION_ACTION"
        const val NOTIFICATION_ACTION_KEY = "NOTIFICATION_ACTION"

        const val NOTIFICATION_EXTRA_KEY = "NOTIFICATION_EXTRA"
    }
}