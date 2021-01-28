package com.yeongil.digitalwellbeing.ui.description

import android.app.Service
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
            descriptionViewModel.ruleNameSubmit()
            findNavController().navigateSafe(directions.actionRuleNameEditDialogToDescriptionFragment())
        }
        binding.ruleName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == KeyEvent.KEYCODE_ENDCALL) {
                descriptionViewModel.ruleNameSubmit()
                findNavController().navigateSafe(directions.actionRuleNameEditDialogToDescriptionFragment())
                false
            } else true
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.ruleName.requestFocus()
        binding.ruleName.selectAll()
        val inputMethodManager =
            dialog?.context?.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.ruleName, 0)
    }

    private fun initViewModel() = run { descriptionViewModel.initEditingRuleName() }
}