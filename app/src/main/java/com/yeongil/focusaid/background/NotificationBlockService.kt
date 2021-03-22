package com.yeongil.focusaid.background

import android.app.Notification
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import androidx.core.content.edit
import com.yeongil.focusaid.data.rule.action.NotificationAction
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class NotificationBlockService : NotificationListenerService() {
    private val sharedPref by lazy {
        getSharedPreferences(NOTI_BLOCK_SERVICE_PREF_NAME, MODE_PRIVATE)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent == null) return START_STICKY
        val action = intent.getParcelableExtra<NotificationAction?>(NOTIFICATION_EXTRA_KEY)
        sharedPref.edit {
            putString(NOTIFICATION_ACTION_KEY, Json.encodeToString(action))
            commit()
        }

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
        val action = getNotificationAction() ?: return

        val pkgName by lazy { sbn.packageName }

        val extra by lazy { sbn.notification.extras }
        val title by lazy { extra.getString(Notification.EXTRA_TITLE) ?: "" }
        val text by lazy { extra.getString(Notification.EXTRA_TEXT) ?: "" }
        val subtext by lazy { extra.getString(Notification.EXTRA_SUB_TEXT) ?: "" }
        val message by lazy { extra.getString(Notification.EXTRA_TEXT_LINES) ?: "" }

        val triggered = (action.allApp || pkgName in action.appList) &&
                action.keywordList.any {
                    if (it.inclusion)
                        it.keyword in title || it.keyword in text || it.keyword in subtext || it.keyword in message
                    else
                        it.keyword !in title && it.keyword !in text && it.keyword !in subtext && it.keyword !in message
                }

        if (triggered) cancelNotification(sbn.key)
    }


    companion object {
        const val NOTI_BLOCK_SERVICE_PREF_NAME =
            "com.yeongil.focusaid.NOTIFICATION_BLOCK_SERVICE"

        const val NOTIFICATION_ACTION_KEY = "NOTIFICATION_ACTION"
        const val NOTIFICATION_EXTRA_KEY = "NOTIFICATION_EXTRA"
    }
}