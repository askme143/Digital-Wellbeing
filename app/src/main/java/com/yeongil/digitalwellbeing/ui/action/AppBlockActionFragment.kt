package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.databinding.FragmentAppBlockActionBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.AppBlockActionViewModel
import com.yeongil.digitalwellbeing.viewModel.AppBlockEntryViewModel
import com.yeongil.digitalwellbeing.viewModel.AppListViewModel
import com.yeongil.digitalwellbeing.viewModel.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModel.itemViewModel.AppItemViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.AppBlockActionViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.AppListViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

class AppBlockActionFragment : Fragment() {
    private var _binding: FragmentAppBlockActionBinding? = null
    private val binding get() = _binding!!

    private val directions = AppBlockActionFragmentDirections

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }
    private val appBlockActionViewModel by activityViewModels<AppBlockActionViewModel> {
        AppBlockActionViewModelFactory(requireContext())
    }
    private val appBlockEntryViewModel by activityViewModels<AppBlockEntryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppBlockActionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = appBlockActionViewModel

        initViewModel()

        appBlockActionViewModel.itemClickEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { packageName ->
                val entry = appBlockActionViewModel.appBlockEntryList.value!!
                    .filter { it.packageName == packageName }[0]
                appBlockEntryViewModel.init(entry)
                findNavController().navigateSafe(directions.actionAppBlockActionFragmentToAppBlockEntryDialog())
            }
        }
        appBlockActionViewModel.itemClickDeleteEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { packageName ->
                with(appBlockActionViewModel.appBlockEntryList) {
                    val oldEntryList = this.value ?: listOf()
                    val index = oldEntryList.map { it.packageName }.indexOf(packageName)
                    this.value = oldEntryList - oldEntryList[index]
                }
            }
        }

        binding.addBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionAppBlockActionFragmentToAppBlockListFragment())
        }
        binding.beforeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionAppBlockActionFragmentToActionEditFragment())
        }
        binding.completeBtn.setOnClickListener {
            ruleEditViewModel.addAppBlockAction(appBlockActionViewModel.getAppBlockAction())
            findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }

        return binding.root
    }

    private fun initViewModel() {
        val action = ruleEditViewModel.editingRule.value?.appBlockAction

        if (action != null) {
            appBlockActionViewModel.init(action)
        } else appBlockActionViewModel.init()
    }
}