package com.yeongil.focusaid.ui.confirm

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.focusaid.background.MainService
import com.yeongil.focusaid.databinding.FragmentConfirmBinding
import com.yeongil.focusaid.utils.navigateSafe
import com.yeongil.focusaid.viewModel.viewModel.rule.DescriptionViewModel
import com.yeongil.focusaid.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.focusaid.viewModelFactory.DescriptionViewModelFactory
import com.yeongil.focusaid.viewModelFactory.RuleEditViewModelFactory

class ConfirmFragment : Fragment() {
    private var _binding: FragmentConfirmBinding? = null
    private val binding get() = _binding!!

    private val directions = ConfirmFragmentDirections

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
        _binding = FragmentConfirmBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = ruleEditViewModel

        binding.beforeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionConfirmFragmentToActionFragment())
        }
        binding.nextBtn.setOnClickListener {
            if (ruleEditViewModel.isNewRule) {
                findNavController().navigateSafe(directions.actionConfirmFragmentToConfirmDialog())
            } else {
                val rule = ruleEditViewModel.editingRule.value!!
                descriptionViewModel.putRule(rule)
                ruleEditViewModel.saveRule()

                val intent = Intent(requireContext(), MainService::class.java)
                intent.action = MainService.RULE_CHANGE
                intent.putExtra(MainService.CHANGED_RULE_ID_KEY, rule.ruleInfo.ruleId)
                requireActivity().startService(intent)

                findNavController().navigateSafe(directions.actionConfirmFragmentToDescriptionFragment())
            }
        }

        return binding.root
    }
}