package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.digitalwellbeing.databinding.DialogAppBlockEntryBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe

class AppBlockEntryDialog : BottomSheetDialogFragment() {
    private var _binding: DialogAppBlockEntryBinding? = null
    private val binding get() = _binding!!

    private val directions = AppBlockEntryDialogDirections

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogAppBlockEntryBinding.inflate(inflater, container, false)

        binding.timePicker.setIs24HourView(true)

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionAppBlockEntryDialogToAppBlockActionFragment())
        }
        binding.completeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionAppBlockEntryDialogToAppBlockActionFragment())
        }

        return binding.root
    }
}