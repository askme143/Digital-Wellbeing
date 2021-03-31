package com.yeongil.focusaid.ui.trigger

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.focusaid.databinding.DialogActivityTriggerBinding
import com.yeongil.focusaid.utils.navigateSafe
import com.yeongil.focusaid.viewModel.viewModel.trigger.ActivityTriggerViewModel
import com.yeongil.focusaid.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.focusaid.viewModelFactory.RuleEditViewModelFactory

class ActivityTriggerDialog : BottomSheetDialogFragment() {
    private var _binding: DialogActivityTriggerBinding? = null
    private val binding get() = _binding!!

    private val directions = ActivityTriggerDialogDirections

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }
    private val activityTriggerViewModel by activityViewModels<ActivityTriggerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogActivityTriggerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = activityTriggerViewModel

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            checkPermission()
        initViewModel()

        binding.cancelBtn.setOnClickListener {
            val goToEditFragment = ruleEditViewModel.editingRule.value?.activityTrigger == null
            if (goToEditFragment)
                findNavController().navigateSafe(directions.actionActivityTriggerDialogToTriggerEditFragment())
            else
                findNavController().navigateSafe(directions.actionGlobalTriggerFragment())
        }
        binding.completeBtn.setOnClickListener {
            ruleEditViewModel.addTriggerAction(activityTriggerViewModel.getActivityTrigger())
            findNavController().navigateSafe(directions.actionGlobalTriggerFragment())
        }

        return binding.root
    }

    private fun initViewModel() {
        val trigger = ruleEditViewModel.editingRule.value?.activityTrigger

        if (trigger != null) {
            activityTriggerViewModel.init(trigger)
        } else activityTriggerViewModel.init()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkPermission() {
        if (
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACTIVITY_RECOGNITION)) {
                Toast.makeText(
                    context, "활동 감지를 위해서는 권한을 설정해야 합니다",
                    Toast.LENGTH_LONG
                ).show()
            }
            requestPermissions(arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (!(grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
            ) {
                findNavController().navigateSafe(directions.actionActivityTriggerDialogToTriggerEditFragment())
                Toast.makeText(
                    context, "활동 감지를 위해서는 설정에서 신체 활동 권한을 허용해야 합니다",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}