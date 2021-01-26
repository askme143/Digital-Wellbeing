package com.yeongil.digitalwellbeing.ui.confirm

import android.app.Service
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.RuleDatabase
import com.yeongil.digitalwellbeing.databinding.DialogConfirmBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

class ConfirmDialog : BottomSheetDialogFragment() {
    private var _binding: DialogConfirmBinding? = null
    private val binding get() = _binding!!

    private val directions = ConfirmDialogDirections

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogConfirmBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = ruleEditViewModel

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.ruleName.requestFocus()
        val inputMethodManager =
            dialog?.context?.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.ruleName, 0)

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionConfirmDialogToConfirmFragment())
        }
        binding.completeBtn.setOnClickListener {
            ruleEditViewModel.saveRule()
            if (ruleEditViewModel.isNewRule)
                findNavController().navigateSafe(directions.actionConfirmDialogToMainFragment())
            else
                findNavController().navigateSafe(directions.actionConfirmDialogToDescriptionFragment())
        }

        return binding.root
    }
}