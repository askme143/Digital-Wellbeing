package com.yeongil.digitalwellbeing.ui.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.databinding.DialogDeleteConfirmBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.DescriptionViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.DescriptionViewModelFactory

class DeleteConfirmDialog : DialogFragment() {
    private var _binding: DialogDeleteConfirmBinding? = null
    private val binding get() = _binding!!

    private val directions = DeleteConfirmDialogDirections

    private val descriptionViewModel by activityViewModels<DescriptionViewModel> {
        DescriptionViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogDeleteConfirmBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = descriptionViewModel

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionDeleteConfirmDialogToDescriptionFragment())
        }
        binding.completeBtn.setOnClickListener {
            descriptionViewModel.deleteRule()
            findNavController().navigateSafe(directions.actionDeleteConfirmDialogToMainFragment())
        }

        return binding.root
    }
}