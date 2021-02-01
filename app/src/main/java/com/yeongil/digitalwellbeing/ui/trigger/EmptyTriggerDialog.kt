package com.yeongil.digitalwellbeing.ui.trigger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.digitalwellbeing.databinding.DialogEmptyTriggerBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe

class EmptyTriggerDialog : BottomSheetDialogFragment() {
    private var _binding: DialogEmptyTriggerBinding? = null
    private val binding get() = _binding!!

    private val directions = EmptyTriggerDialogDirections

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogEmptyTriggerBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionEmptyTriggerDialogToTriggerFragment())
        }
        binding.completeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionEmptyTriggerDialogToActionFragment())
        }

        return binding.root
    }
}