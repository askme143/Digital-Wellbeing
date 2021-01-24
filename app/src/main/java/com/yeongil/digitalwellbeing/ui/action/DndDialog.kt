package com.yeongil.digitalwellbeing.ui.action

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.RuleDatabase
import com.yeongil.digitalwellbeing.databinding.DialogDndBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.DndActionViewModel
import com.yeongil.digitalwellbeing.viewModel.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

class DndDialog : DialogFragment() {
    private var _binding: DialogDndBinding? = null
    private val binding get() = _binding!!

    private val directions = DndDialogDirections

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }
    private val dndActionViewModel by activityViewModels<DndActionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDndBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener {
            val editing = ruleEditViewModel.editingRule.value?.dndAction != null
            if (editing) {
                findNavController().navigateSafe(directions.actionGlobalActionFragment())
            } else {
                findNavController().navigateSafe(directions.actionDndDialogToActionEditFragment())
            }
        }
        binding.completeBtn.setOnClickListener {
            ruleEditViewModel.addDndAction(dndActionViewModel.getDndAction())
            findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }

        return binding.root
    }
}