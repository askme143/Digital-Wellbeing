package com.yeongil.focusaid.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val mainIntent = Intent(context, MainService::class.java)
                .apply { action = MainService.START_BACKGROUND }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(mainIntent)
            else
                context.startService(mainIntent)
        }
    }
}