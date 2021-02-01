package com.yeongil.digitalwellbeing.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.databinding.DialogDeleteConfirmBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.RuleInfoViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.RuleInfoViewModelFactory

class DeleteConfirmDialog : DialogFragment() {
    private var _binding: DialogDeleteConfirmBinding? = null
    private val binding get() = _binding!!

    private val directions = DeleteConfirmDialogDirections

    private val ruleInfoViewModel by activityViewModels<RuleInfoViewModel> {
        RuleInfoViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDeleteConfirmBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = ruleInfoViewModel

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionDeleteConfirmDialogToMainFragment())
        }
        binding.completeBtn.setOnClickListener {
            ruleInfoViewModel.deleteRule()
            findNavController().navigateSafe(directions.actionDeleteConfirmDialogToMainFragment())
        }

        return binding.root
    }
}