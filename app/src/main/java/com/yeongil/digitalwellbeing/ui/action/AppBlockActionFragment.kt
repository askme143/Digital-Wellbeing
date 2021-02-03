package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.data.action.AppBlockEntry
import com.yeongil.digitalwellbeing.databinding.DialogAllAppBlockEntryBinding
import com.yeongil.digitalwellbeing.databinding.FragmentAppBlockActionBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.AppBlockActionViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.AppBlockEntryViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.AppListViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.RuleEditViewModel
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
    private val appListViewModel by activityViewModels<AppListViewModel> {
        AppListViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppBlockActionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = appBlockActionViewModel

        appBlockActionViewModel.itemClickEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { entry ->
                appBlockEntryViewModel.putAppBlockEntry(entry)
                findNavController().navigateSafe(directions.actionAppBlockActionFragmentToAppBlockEntryDialog())
            }
        }

        appBlockActionViewModel.allAppItemClickEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                appBlockEntryViewModel.putAllApp(it)
                val binding = DataBindingUtil.inflate<DialogAllAppBlockEntryBinding>(
                    LayoutInflater.from(requireContext()),
                    R.layout.dialog_all_app_block_entry,
                    null,
                    false
                )
                binding.lifecycleOwner = viewLifecycleOwner
                binding.vm = appBlockEntryViewModel

                val dialog = BottomSheetDialog(requireContext())
                dialog.setContentView(binding.root)
                dialog.show()

                dialog.findViewById<Button>(R.id.cancel_btn)!!
                    .setOnClickListener {
                        dialog.dismiss()
                    }
                dialog.findViewById<Button>(R.id.complete_btn)!!
                    .setOnClickListener {
                        val allAppHandlingAction = appBlockEntryViewModel.getAllAppHandlingAction()
                        appBlockActionViewModel.updateAllAppHandlingAction(allAppHandlingAction)
                        dialog.dismiss()
                    }
            }
        }

        binding.addBtn.setOnClickListener {
            val appList = appBlockActionViewModel.getAppList()
            if (appList == null) appListViewModel.putAllApp()
            else appListViewModel.putAppList(appList)

            findNavController().navigateSafe(directions.actionAppBlockActionFragmentToAppBlockListFragment())
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) { onStartGoBack() }
        binding.beforeBtn.setOnClickListener { onStartGoBack() }
        binding.completeBtn.setOnClickListener {
            ruleEditViewModel.addTriggerAction(appBlockActionViewModel.getAppBlockAction()!!)
            findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }

        return binding.root
    }

    private fun onStartGoBack() {
        val currentAction = appBlockActionViewModel.getAppBlockAction()
        val originalAction = appBlockActionViewModel.originalAction

        if (currentAction != originalAction) {
            val bottomSheetDialog = BottomSheetDialog(requireContext())
            bottomSheetDialog.setContentView(R.layout.dialog_cancel_confirm)
            bottomSheetDialog.show()

            bottomSheetDialog.findViewById<Button>(R.id.complete_btn)!!
                .setOnClickListener {
                    bottomSheetDialog.dismiss()
                    goBack()
                }
            bottomSheetDialog.findViewById<Button>(R.id.cancel_btn)!!
                .setOnClickListener { bottomSheetDialog.dismiss() }
        } else {
            goBack()
        }
    }

    private fun goBack() {
        if (appBlockActionViewModel.originalAction == null) {
            findNavController().navigateSafe(directions.actionAppBlockActionFragmentToActionEditFragment())
        } else {
            findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }
    }
}