package com.yeongil.focusaid.background

import android.app.Notification
import android.content.Intent
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.SpannableString
import android.util.Log
import androidx.core.content.edit
import com.yeongil.focusaid.data.rule.action.KeywordEntry
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

        val triggered =
            (action.allApp || pkgName in action.appList) && checkKeyword(extra, action.keywordList)

        if (triggered) cancelNotification(sbn.key)
    }

    private fun checkKeyword(extra: Bundle, keywordList: List<KeywordEntry>): Boolean {
        val messages =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                extra.get(Notification.EXTRA_MESSAGES)
                    ?.let { if (it is SpannableString) it.toString() else it.toString() } ?: ""
            } else ""
        val extraMessagingPerson =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                extra.get(Notification.EXTRA_MESSAGING_PERSON)
                    ?.let { if (it is SpannableString) it.toString() else it.toString() } ?: ""
            } else ""
        val conversationTitle =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                extra.get(Notification.EXTRA_CONVERSATION_TITLE)
                    ?.let { if (it is SpannableString) it.toString() else it.toString() } ?: ""
            } else ""
        val title = extra.get(Notification.EXTRA_TITLE)
            ?.let { if (it is SpannableString) it.toString() else it.toString() }
            ?: ""
        val titleBig = extra.get(Notification.EXTRA_TITLE_BIG)
            ?.let { if (it is SpannableString) it.toString() else it.toString() }
            ?: ""
        val text = extra.get(Notification.EXTRA_TEXT)
            ?.let { if (it is SpannableString) it.toString() else it.toString() }
            ?: ""
        val subText = extra.get(Notification.EXTRA_SUB_TEXT)
            ?.let { if (it is SpannableString) it.toString() else it.toString() }
            ?: ""
        val summaryText = extra.get(Notification.EXTRA_SUMMARY_TEXT)
            ?.let { if (it is SpannableString) it.toString() else it.toString() }
            ?: ""
        val infoText = extra.get(Notification.EXTRA_INFO_TEXT)
            ?.let { if (it is SpannableString) it.toString() else it.toString() }
            ?: ""
        val bigText = extra.get(Notification.EXTRA_BIG_TEXT)
            ?.let { if (it is SpannableString) it.toString() else it.toString() }
            ?: ""
        val textLines = extra.get(Notification.EXTRA_TEXT_LINES)
            ?.let { if (it is SpannableString) it.toString() else it.toString() }
            ?: ""

        val strList = listOf(
            messages,
            extraMessagingPerson,
            conversationTitle,
            title,
            titleBig,
            text,
            subText,
            summaryText,
            infoText,
            bigText,
            textLines
        )

        return keywordList.any {
            val keyword = it.keyword
            if (it.inclusion)
                strList.any { str -> keyword in str }
            else
                !strList.any { str -> keyword in str }
        }
    }


    companion object {
        const val NOTI_BLOCK_SERVICE_PREF_NAME =
            "com.yeongil.focusaid.NOTIFICATION_BLOCK_SERVICE"

        const val NOTIFICATION_ACTION_KEY = "NOTIFICATION_ACTION"
        const val NOTIFICATION_EXTRA_KEY = "NOTIFICATION_EXTRA"
    }
}