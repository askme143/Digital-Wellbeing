package com.yeongil.focusaid.ui.main

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.focusaid.databinding.DialogRuleDeleteConfirmBinding
import com.yeongil.focusaid.utils.navigateSafe
import com.yeongil.focusaid.viewModel.viewModel.rule.RuleInfoViewModel
import com.yeongil.focusaid.viewModelFactory.RuleInfoViewModelFactory

class RuleDeleteConfirmDialog : DialogFragment() {
    private var _binding: DialogRuleDeleteConfirmBinding? = null
    private val binding get() = _binding!!

    private val directions = RuleDeleteConfirmDialogDirections

    private val ruleInfoViewModel by activityViewModels<RuleInfoViewModel> {
        RuleInfoViewModelFactory(requireContext())
    }

    private val windowManager by lazy {
        requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRuleDeleteConfirmBinding.inflate(inflater, container, false)
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

    @Suppress("DEPRECATION")
    override fun onResume() {
        super.onResume()

        val display =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R)
                requireContext().display ?: return
            else
                windowManager.defaultDisplay ?: return

        val size = Point()
        display.getSize(size)

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        params?.width = (size.x * 0.75).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }
}