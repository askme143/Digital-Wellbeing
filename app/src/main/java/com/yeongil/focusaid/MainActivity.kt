package com.yeongil.focusaid

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.yeongil.focusaid.background.MainService
import com.yeongil.focusaid.viewModel.viewModel.action.AppListViewModel
import com.yeongil.focusaid.viewModelFactory.AppListViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    private val appListViewModel by viewModels<AppListViewModel> {
        AppListViewModelFactory(applicationContext)
    }

    @SuppressLint("BatteryLife")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job = Job()

        /* Do some heavy tasks */
        lifecycleScope.launch(Dispatchers.Default) {
            load()
        }

        /* Request to turn off battery optimization for this app */
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        val isWhiteListing =
            powerManager.isIgnoringBatteryOptimizations(packageName)
        if (!isWhiteListing) {
            val intent = Intent()
            intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            intent.data = Uri.parse("package:$packageName")
            startActivity(intent)
        }

        /* Start Background Service */
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(Int.MAX_VALUE)
        if (MainService::class.java.name !in runningServices.map { it.service.className }) {
            val intent = Intent(this, MainService::class.java)
                .apply { action = MainService.START_BACKGROUND }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }
    }

    override fun onResume() {
        Log.e("hello", "main resume")
        super.onResume()
    }

    private suspend fun load() {
        appListViewModel.loadAppList()  // load app lists with sorting
    }
}