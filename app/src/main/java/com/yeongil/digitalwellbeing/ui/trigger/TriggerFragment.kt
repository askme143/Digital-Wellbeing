package com.yeongil.digitalwellbeing.ui.trigger

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.data.database.RuleDatabase
import com.yeongil.digitalwellbeing.databinding.FragmentTriggerBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

class TriggerFragment : Fragment() {
    private var _binding: FragmentTriggerBinding? = null
    private val binding get() = _binding!!

    private val directions = TriggerFragmentDirections

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        val ruleDao = RuleDatabase.getInstance(requireContext().applicationContext).ruleDao()
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        RuleEditViewModelFactory(ruleDao, sharedPref)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTriggerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = ruleEditViewModel

        binding.addBtn.setOnClickListener { findNavController().navigateSafe(directions.actionTriggerFragmentToTriggerNestedGraph()) }
        binding.beforeBtn.setOnClickListener { findNavController().navigateSafe(directions.actionTriggerFragmentToMainFragment()) }
        binding.nextBtn.setOnClickListener { findNavController().navigateSafe(directions.actionTriggerFragmentToActionFragment()) }

        return binding.root
    }
}