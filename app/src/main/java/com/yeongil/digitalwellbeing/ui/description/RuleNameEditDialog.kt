package com.yeongil.digitalwellbeing.ui.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.digitalwellbeing.databinding.DialogRuleNameEditBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.DescriptionViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.DescriptionViewModelFactory

class RuleNameEditDialog : BottomSheetDialogFragment() {
    private var _binding: DialogRuleNameEditBinding? = null
    private val binding get() = _binding!!

    private val directions = RuleNameEditDialogDirections

    private val descriptionViewModel by activityViewModels<DescriptionViewModel> {
        DescriptionViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogRuleNameEditBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = descriptionViewModel

        initViewModel()

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionRuleNameEditDialogToDescriptionFragment())
        }
        binding.completeBtn.setOnClickListener {
            // TODO: Check if the rule name is changed correctly
            descriptionViewModel.ruleNameSubmit()
            findNavController().navigateSafe(directions.actionRuleNameEditDialogToDescriptionFragment())
        }

        return binding.root
    }

    private fun initViewModel() = run { descriptionViewModel.initEditingRuleName() }
}