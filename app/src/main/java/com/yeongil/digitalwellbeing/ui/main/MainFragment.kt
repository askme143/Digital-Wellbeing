package com.yeongil.digitalwellbeing.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.data.database.RuleDatabase
import com.yeongil.digitalwellbeing.databinding.FragmentMainBinding
import com.yeongil.digitalwellbeing.viewModel.RuleViewModel
import com.yeongil.digitalwellbeing.viewModel.RuleViewModelFactory

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val ruleViewModel by activityViewModels<RuleViewModel> {
        RuleViewModelFactory(
            RuleDatabase.getInstance(requireContext().applicationContext).ruleDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = ruleViewModel

        binding.addBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_triggerFragment)
        }

        return binding.root
    }
}