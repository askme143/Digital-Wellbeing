package com.yeongil.digitalwellbeing

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import com.yeongil.digitalwellbeing.background.MainNotification


class TestService : Service() {
    private val activityClient by lazy { ActivityRecognition.getClient(this) }
    private val pendingIntent by lazy {
        Intent(this, TestService::class.java).let {
            PendingIntent.getService(this, 0, it, 0)
        }
    }
    private val notificationBuilder by lazy {
        NotificationCompat.Builder(this, MainNotification.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText("테스트 중")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(
                TaskStackBuilder.create(this).run {
                    addNextIntentWithParentStack(
                        Intent(this@TestService, MainActivity::class.java)
                    )
                    getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
                }
            )
            .setOngoing(true)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (ActivityTransitionResult.hasResult(intent)) {
            val result = ActivityTransitionResult.extractResult(intent)!!
            notificationBuilder.setContentText(result.transitionEvents.joinToString { it.activityType.toString() })
            startForeground(1, notificationBuilder.build())
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                MainNotification.CHANNEL_ID,
                "Digitall Wellbeing Channel",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Digital Wellbeing Background"
                setShowBadge(false)
            }

            val manager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(serviceChannel)
        }

        startForeground(1, notificationBuilder.build())
        requestActivityTransitionUpdates()
    }

    private fun requestActivityTransitionUpdates() {
        val transitions = listOf(
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build(),
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.IN_VEHICLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build(),
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_BICYCLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build(),
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.ON_BICYCLE)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build(),
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                .build(),
            ActivityTransition.Builder()
                .setActivityType(DetectedActivity.STILL)
                .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                .build(),
        )

        val request = ActivityTransitionRequest(transitions)
        val task = ActivityRecognition.getClient(this)
            .requestActivityTransitionUpdates(request, pendingIntent)

        task.addOnSuccessListener {
            Log.e("hello", "hello")
        }

        task.addOnFailureListener {
            Log.e("hello", "hello")
        }
    }
}