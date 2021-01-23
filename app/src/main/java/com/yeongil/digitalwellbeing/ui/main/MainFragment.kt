package com.yeongil.digitalwellbeing.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.database.ruleDatabase.RuleDatabase
import com.yeongil.digitalwellbeing.databinding.FragmentMainBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.MainViewModel
import com.yeongil.digitalwellbeing.viewModel.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.MainViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel by activityViewModels<MainViewModel> {
        MainViewModelFactory(
            RuleDatabase.getInstance(requireContext().applicationContext).ruleDao()
        )
    }
    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        val ruleDao = RuleDatabase.getInstance(requireContext().applicationContext).ruleDao()
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        RuleEditViewModelFactory(ruleDao, sharedPref)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = mainViewModel

        binding.addBtn.setOnClickListener {
            ruleEditViewModel.init()
            findNavController().navigateSafe(MainFragmentDirections.actionMainFragmentToTriggerFragment())
        }

        return binding.root
    }
}