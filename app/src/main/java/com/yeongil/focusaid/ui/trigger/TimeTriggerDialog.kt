package com.yeongil.focusaid.ui.trigger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.focusaid.databinding.DialogTimeTriggerBinding
import com.yeongil.focusaid.utils.navigateSafe
import com.yeongil.focusaid.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.focusaid.viewModel.viewModel.trigger.TimeTriggerViewModel
import com.yeongil.focusaid.viewModelFactory.RuleEditViewModelFactory

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
            val goToEditFragment = ruleEditViewModel.editingRule.value?.timeTrigger == null
            if (goToEditFragment)
                findNavController().navigateSafe(directions.actionTimeTriggerDialogToTriggerEditFragment())
            else
                findNavController().navigateSafe(directions.actionGlobalTriggerFragment())
        }
        binding.completeBtn.setOnClickListener {
            ruleEditViewModel.addTriggerAction(timeTriggerViewModel.getTimeTrigger())
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