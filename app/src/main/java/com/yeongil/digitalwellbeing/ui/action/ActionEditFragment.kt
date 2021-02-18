package com.yeongil.digitalwellbeing.ui.action

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentActionEditBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.AppBlockActionViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.NotificationActionViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.RingerActionViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.AppBlockActionViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.NotificationActionViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActionEditBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener { findNavController().navigateSafe(directions.actionGlobalActionFragment()) }

        binding.appTimeBtn.setOnClickListener {
            appBlockActionViewModel.putAppBlockAction(ruleEditViewModel.editingRule.value!!.appBlockAction)
            findNavController().navigateSafe(directions.actionActionEditFragmentToAppBlockActionFragment())
        }
        binding.notificationBtn.setOnClickListener {
            notificationViewModel.putNotificationAction(ruleEditViewModel.editingRule.value!!.notificationAction)
            findNavController().navigateSafe(directions.actionActionEditFragmentToNotificationActionFragment())
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
        dialog.setContentView(R.layout.dialog_dnd_permission)
        dialog.create()

        dialog.findViewById<Button>(R.id.confirm_btn)!!.setOnClickListener {
            requireContext().startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
            dialog.dismiss()
        }
        dialog.show()
    }
}