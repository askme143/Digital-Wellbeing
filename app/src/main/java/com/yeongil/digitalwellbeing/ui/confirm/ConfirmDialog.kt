package com.yeongil.digitalwellbeing.ui.confirm

import android.app.Service
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.digitalwellbeing.databinding.DialogConfirmBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.DescriptionViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.DescriptionViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class ConfirmDialog : BottomSheetDialogFragment() {
    private var _binding: DialogConfirmBinding? = null
    private val binding get() = _binding!!

    private val directions = ConfirmDialogDirections

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }
    private val descriptionViewModel by activityViewModels<DescriptionViewModel> {
        DescriptionViewModelFactory(requireContext())
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

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionConfirmDialogToConfirmFragment())
        }
        binding.completeBtn.setOnClickListener { onComplete() }
        binding.ruleName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == KeyEvent.KEYCODE_ENDCALL) {
                onComplete(); false
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

    private fun onComplete() {
        ruleEditViewModel.saveRule()
        if (ruleEditViewModel.isNewRule) {
            findNavController().navigateSafe(directions.actionConfirmDialogToMainFragment())
        } else {
            descriptionViewModel.init(ruleEditViewModel.editingRule.value!!)
            findNavController().navigateSafe(directions.actionConfirmDialogToDescriptionFragment())
        }
    }
}