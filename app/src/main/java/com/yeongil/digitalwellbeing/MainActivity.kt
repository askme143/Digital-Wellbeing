package com.yeongil.digitalwellbeing

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    @SuppressLint("BatteryLife")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job = Job()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val pm = applicationContext.getSystemService(POWER_SERVICE) as PowerManager
            val isWhiteListing = pm.isIgnoringBatteryOptimizations(applicationContext.packageName)

            if (!isWhiteListing) {
                val intent = Intent()
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:" + applicationContext.packageName)
                startActivity(intent)
            }
        }

        val intent = Intent(this, MainService::class.java)
        startService(intent)
    }
}