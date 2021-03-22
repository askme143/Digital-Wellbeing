package com.yeongil.focusaid.ui.trigger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
}