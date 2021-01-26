package com.yeongil.digitalwellbeing.ui.trigger

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.RuleDatabase
import com.yeongil.digitalwellbeing.databinding.DialogActivityTriggerBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.ActivityTriggerViewModel
import com.yeongil.digitalwellbeing.viewModel.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

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
            val editing = ruleEditViewModel.editingRule.value?.activityTrigger != null
            if (editing)
                findNavController().navigateSafe(directions.actionGlobalTriggerFragment())
            else
                findNavController().navigateSafe(directions.actionActivityTriggerDialogToTriggerEditFragment())
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