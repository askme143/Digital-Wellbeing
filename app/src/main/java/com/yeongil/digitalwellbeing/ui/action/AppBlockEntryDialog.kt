package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.digitalwellbeing.databinding.DialogAppBlockEntryBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.AppBlockActionViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.AppBlockEntryViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.AppBlockActionViewModelFactory

class AppBlockEntryDialog : BottomSheetDialogFragment() {
    private var _binding: DialogAppBlockEntryBinding? = null
    private val binding get() = _binding!!

    private val directions = AppBlockEntryDialogDirections

    private val appBlockActionViewModel by activityViewModels<AppBlockActionViewModel> {
        AppBlockActionViewModelFactory(requireContext())
    }
    private val appBlockEntryViewModel by activityViewModels<AppBlockEntryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogAppBlockEntryBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = appBlockEntryViewModel

        binding.timePicker.setIs24HourView(true)

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionAppBlockEntryDialogToAppBlockActionFragment())
        }
        binding.completeBtn.setOnClickListener {
            val entry = appBlockEntryViewModel.getAppBlockEntry()
            appBlockActionViewModel.updateAppBlockEntryList(entry)
            findNavController().navigateSafe(directions.actionAppBlockEntryDialogToAppBlockActionFragment())
        }

        return binding.root
    }
}