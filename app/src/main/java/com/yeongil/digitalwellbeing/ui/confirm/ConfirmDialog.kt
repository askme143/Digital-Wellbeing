package com.yeongil.digitalwellbeing.ui.confirm

import android.app.Service
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.DialogConfirmBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe

class ConfirmDialog : BottomSheetDialogFragment() {
    private var _binding: DialogConfirmBinding? = null;
    private val binding get() = _binding!!

    private val directions = ConfirmDialogDirections

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogConfirmBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        binding.ruleName.requestFocus()
        val inputMethodManager =
            dialog?.context?.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.ruleName, 0)

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionConfirmDialogToConfirmFragment())
        }
        binding.completeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionConfirmDialogToMainFragment())
        }

        return binding.root
    }
}