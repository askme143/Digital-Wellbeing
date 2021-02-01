package com.yeongil.digitalwellbeing.background

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.yeongil.digitalwellbeing.MainActivity
import com.yeongil.digitalwellbeing.R

object MainNotification {
    const val CHANNEL_ID = "digital_wellbeing_service_channel"

    fun createNotification(context: Context): Notification {
        val notification =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText("실행 중")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(
                    TaskStackBuilder.create(context).run {
                        addNextIntentWithParentStack(
                            Intent(context, MainActivity::class.java)
                        )
                        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
                    }
                )
                .setOngoing(true)
                .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Digitall Wellbeing Channel",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Digital Wellbeing Background"
                setShowBadge(false)
            }

            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(serviceChannel)
        }

        return notification
    }
}