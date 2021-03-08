package com.yeongil.digitalwellbeing

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.WindowManager.LayoutParams
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.yeongil.digitalwellbeing.background.AppBlockService
import com.yeongil.digitalwellbeing.databinding.DialogAppBlockAlertBinding
import com.yeongil.digitalwellbeing.databinding.DialogAppBlockCloseBinding
import com.yeongil.digitalwellbeing.viewModel.viewModel.AppBlockViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.AppBlockViewModelFactory


class AppBlockActivity : AppCompatActivity() {
    private val appBlockViewModel: AppBlockViewModel by viewModels {
        AppBlockViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* Initialize window settings */
        window.setFlags(LayoutParams.FLAG_NOT_TOUCH_MODAL, LayoutParams.FLAG_NOT_TOUCH_MODAL)
        window.setFlags(
            LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        )

        /* Initialize */
        val packageName = intent.getStringExtra(PACKAGE_NAME_KEY) ?: return
        val blockingAppKey = intent.getStringExtra(BLOCKING_APP_KEY_KEY) ?: return
        appBlockViewModel.putPackageName(packageName)
        appBlockViewModel.putBlockingAppKey(blockingAppKey)

        /* Show dialog */
        when (intent.action) {
            CLOSE_IMMEDIATE -> {
                appBlockViewModel.setClose()
                showCloseDialog()
            }
            ALERT -> {
                appBlockViewModel.setAlert()
                showAlertDialog()
            }
        }
    }

    private fun showCloseDialog() {
        val binding: DialogAppBlockCloseBinding =
            DataBindingUtil.setContentView(this, R.layout.dialog_app_block_close)
        binding.vm = appBlockViewModel
        binding.lifecycleOwner = this
        binding.notifyChange()

        binding.tempUseBtn.setOnClickListener { extendAllowedTime() }
        binding.completeBtn.setOnClickListener { goHome() }
        onBackPressedDispatcher.addCallback { goHome() }
    }

    private fun showAlertDialog() {
        val binding: DialogAppBlockAlertBinding =
            DataBindingUtil.setContentView(this, R.layout.dialog_app_block_alert)
        binding.vm = appBlockViewModel
        binding.lifecycleOwner = this
        binding.notifyChange()

        binding.closeBtn.setOnClickListener { goHome() }
        binding.tempUseBtn.setOnClickListener { allowForThisTime() }
        binding.completeBtn.setOnClickListener { extendAllowedTime() }
        onBackPressedDispatcher.addCallback { extendAllowedTime() }
    }

    private fun goHome() {
        val intent = Intent().apply {
            action = "android.intent.action.MAIN"
            addCategory("android.intent.category.HOME")
            addFlags(
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                        or Intent.FLAG_ACTIVITY_FORWARD_RESULT
                        or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
                        or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            )
        }

        finish()
        startActivity(intent)
    }

    private fun extendAllowedTime() {
        val packageName = appBlockViewModel.blockingAppKey
        val intent = Intent(this, AppBlockService::class.java).apply {
            action = AppBlockService.EXTEND_ALLOWED_TIME
            putExtra(AppBlockService.PACKAGE_NAME_EXTRA_KEY, packageName)
        }

        finish()
        startService(intent)
    }

    private fun allowForThisTime() {
        val packageName = appBlockViewModel.blockingAppKey
        val intent = Intent(this, AppBlockService::class.java).apply {
            action = AppBlockService.ALLOW_FOR_THIS_TIME
            putExtra(AppBlockService.PACKAGE_NAME_EXTRA_KEY, packageName)
        }

        finish()
        startService(intent)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        /* If we've received a touch notification that the user has touched
         * outside the app, finish the activity. */
        if (MotionEvent.ACTION_OUTSIDE == event.action) {
            val isClose = appBlockViewModel.isClose.value ?: false
            if (isClose) goHome() else extendAllowedTime()
            return true
        }

        /* Delegate everything else to Activity */
        return super.onTouchEvent(event)
    }

    companion object {
        const val CLOSE_IMMEDIATE = "CLOSE_IMMEDIATE"
        const val ALERT = "ALERT"

        const val PACKAGE_NAME_KEY = "PACKAGE_NAME"
        const val BLOCKING_APP_KEY_KEY = "BLOCKING_APP_KEY"
    }
}