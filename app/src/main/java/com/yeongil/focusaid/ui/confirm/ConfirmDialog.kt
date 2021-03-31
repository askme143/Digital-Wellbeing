package com.yeongil.focusaid.ui.confirm

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.focusaid.background.MainService
import com.yeongil.focusaid.databinding.DialogConfirmBinding
import com.yeongil.focusaid.utils.navigateSafe
import com.yeongil.focusaid.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.focusaid.viewModelFactory.RuleEditViewModelFactory

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

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionConfirmDialogToConfirmFragment())
        }
        binding.completeBtn.setOnClickListener { ruleEditViewModel.saveRule() }
        binding.ruleName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == KeyEvent.KEYCODE_ENDCALL) {
                ruleEditViewModel.saveRule(); false
            } else true
        }

        ruleEditViewModel.clearErrorMessage()
        ruleEditViewModel.insertEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { onComplete() }
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
        val rule = ruleEditViewModel.editingRule.value!!
        val intent = Intent(requireContext(), MainService::class.java)
        intent.action = MainService.RULE_CHANGE
        intent.putExtra(MainService.CHANGED_RULE_ID_KEY, rule.ruleInfo.ruleId)
        requireActivity().startService(intent)

        Toast.makeText(requireContext().applicationContext, "규칙이 생성되었습니다.", Toast.LENGTH_SHORT)
            .show()
        findNavController().navigateSafe(directions.actionConfirmDialogToMainFragment())
    }
}