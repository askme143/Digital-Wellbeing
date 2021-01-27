package com.yeongil.digitalwellbeing.ui.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.databinding.FragmentDescriptionBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.DescriptionViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.DescriptionViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

class DescriptionFragment : Fragment() {
    private var _binding: FragmentDescriptionBinding? = null
    private val binding get() = _binding!!

    val directions = DescriptionFragmentDirections

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
        _binding = FragmentDescriptionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = descriptionViewModel

        binding.ruleName.setOnClickListener {
            findNavController().navigateSafe(directions.actionDescriptionFragmentToRuleNameEditDialog())
        }
        binding.editBtn.setOnClickListener {
            ruleEditViewModel.init(descriptionViewModel.getRule())
            findNavController().navigateSafe(directions.actionDescriptionFragmentToTriggerFragment())
        }
        binding.deleteBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionDescriptionFragmentToDeleteConfirmDialog())
        }
        binding.finishBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionDescriptionFragmentToMainFragment())
        }

        return binding.root
    }
}