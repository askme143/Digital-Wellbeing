package com.yeongil.digitalwellbeing.ui.trigger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.digitalwellbeing.databinding.DialogDeleteConfirmBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

class TriggerDeleteConfirmDialog : BottomSheetDialogFragment() {
    private var _binding: DialogDeleteConfirmBinding? = null
    private val binding get() = _binding!!

    private val directions = TriggerDeleteConfirmDialogDirections

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogDeleteConfirmBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionTriggerDeleteConfirmDialogToTriggerFragment())
        }
        binding.completeBtn.setOnClickListener {
            ruleEditViewModel.deleteItem()
            findNavController().navigateSafe(directions.actionTriggerDeleteConfirmDialogToTriggerFragment())
        }

        return binding.root
    }
}