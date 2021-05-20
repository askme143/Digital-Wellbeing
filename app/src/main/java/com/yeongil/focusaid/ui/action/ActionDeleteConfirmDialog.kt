package com.yeongil.focusaid.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.focusaid.databinding.DialogDeleteConfirmBinding
import com.yeongil.focusaid.utils.navigateSafe
import com.yeongil.focusaid.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.focusaid.viewModelFactory.RuleEditViewModelFactory

class ActionDeleteConfirmDialog : BottomSheetDialogFragment() {
    private var _binding: DialogDeleteConfirmBinding? = null
    private val binding get() = _binding!!

    private val directions = ActionDeleteConfirmDialogDirections

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDeleteConfirmBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionActionDeleteConfirmDialogToActionFragment())
        }
        binding.completeBtn.setOnClickListener {
            ruleEditViewModel.deleteItem()
            findNavController().navigateSafe(directions.actionActionDeleteConfirmDialogToActionFragment())
        }

        return binding.root
    }
}