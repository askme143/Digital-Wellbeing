package com.yeongil.digitalwellbeing.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.databinding.FragmentMainBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.MainViewModel
import com.yeongil.digitalwellbeing.viewModel.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.MainViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    val directions = MainFragmentDirections

    private val mainViewModel by activityViewModels<MainViewModel> {
        MainViewModelFactory(requireContext())
    }
    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = mainViewModel

        mainViewModel.itemClickEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {}
            findNavController().navigateSafe(directions.actionMainFragmentToDescriptionFragment())
        }

        binding.addBtn.setOnClickListener {
            ruleEditViewModel.init()
            findNavController().navigateSafe(directions.actionMainFragmentToTriggerFragment())
        }

        return binding.root
    }
}