package com.yeongil.digitalwellbeing.ui.trigger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.databinding.FragmentTriggerBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModel.item.ACTIVITY_TRIGGER_TITLE
import com.yeongil.digitalwellbeing.viewModel.item.LOCATION_TRIGGER_TITLE
import com.yeongil.digitalwellbeing.viewModel.item.TIME_TRIGGER_TITLE
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

class TriggerFragment : Fragment() {
    private var _binding: FragmentTriggerBinding? = null
    private val binding get() = _binding!!

    private val directions = TriggerFragmentDirections

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTriggerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = ruleEditViewModel

        ruleEditViewModel.itemClickEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    LOCATION_TRIGGER_TITLE ->
                        findNavController().navigateSafe(directions.actionTriggerFragmentToLocationTriggerFragment())
                    TIME_TRIGGER_TITLE ->
                        findNavController().navigateSafe(directions.actionTriggerFragmentToTimeTriggerDialog())
                    ACTIVITY_TRIGGER_TITLE ->
                        findNavController().navigateSafe(directions.actionTriggerFragmentToActivityTriggerDialog())
                }
            }
        }

        binding.addBtn.setOnClickListener { findNavController().navigateSafe(directions.actionTriggerFragmentToTriggerEditFragment()) }
        binding.beforeBtn.setOnClickListener {
            if (ruleEditViewModel.isNewRule) {
                findNavController().navigateSafe(directions.actionTriggerFragmentToMainFragment())
            } else {
                findNavController().navigateSafe(directions.actionTriggerFragmentToDescriptionFragment())
            }
        }
        binding.nextBtn.setOnClickListener { findNavController().navigateSafe(directions.actionTriggerFragmentToActionFragment()) }

        return binding.root
    }
}