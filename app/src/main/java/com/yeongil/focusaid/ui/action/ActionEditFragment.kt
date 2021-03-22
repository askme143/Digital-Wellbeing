package com.yeongil.focusaid.ui.action

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.AppOpsManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yeongil.focusaid.R
import com.yeongil.focusaid.databinding.FragmentActionEditBinding
import com.yeongil.focusaid.utils.navigateSafe
import com.yeongil.focusaid.viewModel.viewModel.action.AppBlockActionViewModel
import com.yeongil.focusaid.viewModel.viewModel.action.NotificationActionViewModel
import com.yeongil.focusaid.viewModel.viewModel.action.RingerActionViewModel
import com.yeongil.focusaid.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.focusaid.viewModelFactory.AppBlockActionViewModelFactory
import com.yeongil.focusaid.viewModelFactory.NotificationActionViewModelFactory
import com.yeongil.focusaid.viewModelFactory.RuleEditViewModelFactory

class ActionEditFragment : Fragment() {
    private var _binding: FragmentActionEditBinding? = null
    private val binding get() = _binding!!

    private val directions = ActionEditFragmentDirections

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }
    private val appBlockActionViewModel by activityViewModels<AppBlockActionViewModel> {
        AppBlockActionViewModelFactory(requireContext())
    }
    private val notificationViewModel by activityViewModels<NotificationActionViewModel> {
        NotificationActionViewModelFactory(requireContext())
    }
    private val ringerActionViewModel by activityViewModels<RingerActionViewModel>()

    private val notificationManager by lazy {
        requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    private val accessibilityManager by lazy {
        requireActivity().getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    }
    private val appOpsManager by lazy {
        requireActivity().getSystemService(AppCompatActivity.APP_OPS_SERVICE) as AppOpsManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActionEditBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener { findNavController().navigateSafe(directions.actionGlobalActionFragment()) }

        binding.appBlockBtn.setOnClickListener {
            val accessibilityPermission = accessibilityManager
                .getEnabledAccessibilityServiceList(AccessibilityServiceInfo.DEFAULT)
                .any {
                    it.resolveInfo.serviceInfo.packageName ==
                            requireContext().applicationContext.packageName
                }
            val usageStatsPermission =
                appOpsManager.checkOpNoThrow(
                    "android:get_usage_stats", Process.myUid(),
                    requireContext().packageName
                ) == AppOpsManager.MODE_ALLOWED

            if (!accessibilityPermission) {
                showAccessibilityPermissionDialog()
            } else if (!usageStatsPermission) {
                showUsageStatsPermissionDialog()
            } else {
                appBlockActionViewModel.putAppBlockAction(ruleEditViewModel.editingRule.value!!.appBlockAction)
                findNavController().navigateSafe(directions.actionActionEditFragmentToAppBlockActionFragment())
            }
        }
        binding.notificationBtn.setOnClickListener {
            val notificationListenerPermission =
                NotificationManagerCompat.getEnabledListenerPackages(requireContext())
                    .contains(requireContext().applicationContext.packageName)
            if (!notificationListenerPermission) {
                showNotificationListenerPermissionDialog()
            } else {
                notificationViewModel.putNotificationAction(ruleEditViewModel.editingRule.value!!.notificationAction)
                findNavController().navigateSafe(directions.actionActionEditFragmentToNotificationActionFragment())
            }
        }
        binding.dndBtn.setOnClickListener {
            if (!notificationManager.isNotificationPolicyAccessGranted) {
                showDndPermissionDialog()
            } else {
                findNavController().navigateSafe(directions.actionActionEditFragmentToDndDialog())
            }
        }
        binding.ringerBtn.setOnClickListener {
            if (!notificationManager.isNotificationPolicyAccessGranted) {
                showDndPermissionDialog()
            } else {
                ringerActionViewModel.putRingerAction(ruleEditViewModel.editingRule.value!!.ringerAction)
                findNavController().navigateSafe(directions.actionActionEditFragmentToRingerDialog())
            }
        }

        return binding.root
    }

    private fun showDndPermissionDialog() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.dialog_permission)
        dialog.create()

        dialog.findViewById<TextView>(R.id.title)!!.text = "방해금지모드 권한을 허용해주세요."
        dialog.findViewById<Button>(R.id.confirm_btn)!!.setOnClickListener {
            requireContext().startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showAccessibilityPermissionDialog() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.dialog_permission)
        dialog.create()

        dialog.findViewById<TextView>(R.id.title)!!.text =
            "접근성 권한을 허용해주세요.\n(설치된 서비스 > FocusAid 허용)"
        dialog.findViewById<Button>(R.id.confirm_btn)!!.setOnClickListener {
            requireContext().startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showUsageStatsPermissionDialog() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.dialog_permission)
        dialog.create()

        dialog.findViewById<TextView>(R.id.title)!!.text =
            "사용 정보 접근 권한을 허용해주세요."
        dialog.findViewById<Button>(R.id.confirm_btn)!!.setOnClickListener {
            requireContext().startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showNotificationListenerPermissionDialog() {
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(R.layout.dialog_permission)
        dialog.create()

        dialog.findViewById<TextView>(R.id.title)!!.text = "알림 접근 권한을 허용해주세요."
        dialog.findViewById<Button>(R.id.confirm_btn)!!.setOnClickListener {
            requireContext().startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
            dialog.dismiss()
        }
        dialog.show()
    }
}