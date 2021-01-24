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
import com.yeongil.digitalwellbeing.databinding.DialogTimeTriggerBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModel.TimeTriggerViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

class TimeTriggerDialog : BottomSheetDialogFragment() {
    private var _binding: DialogTimeTriggerBinding? = null
    private val binding get() = _binding!!

    private val directions = TimeTriggerDialogDirections

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }
    private val timeTriggerViewModel by activityViewModels<TimeTriggerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogTimeTriggerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = timeTriggerViewModel

        initViewModel()

        binding.cancelBtn.setOnClickListener {
            val editing = ruleEditViewModel.editingRule.value?.timeTrigger != null
            if (editing)
                findNavController().navigateSafe(directions.actionGlobalTriggerFragment())
            else
                findNavController().navigateSafe(directions.actionTimeTriggerDialogToTriggerEditFragment())
        }
        binding.completeBtn.setOnClickListener {
            ruleEditViewModel.addTimeTrigger(timeTriggerViewModel.getTimeTrigger())
            findNavController().navigateSafe(directions.actionGlobalTriggerFragment())
        }

        return binding.root
    }

    private fun initViewModel() {
        val trigger = ruleEditViewModel.editingRule.value?.timeTrigger

        if (trigger != null) {
            timeTriggerViewModel.init(trigger)
        } else timeTriggerViewModel.init()
    }
}