package com.yeongil.digitalwellbeing.ui.trigger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentTriggerBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModel.item.ACTIVITY_TRIGGER_TITLE
import com.yeongil.digitalwellbeing.viewModel.item.LOCATION_TRIGGER_TITLE
import com.yeongil.digitalwellbeing.viewModel.item.TIME_TRIGGER_TITLE
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory
import kotlin.system.measureNanoTime

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

        ruleEditViewModel.itemAddEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(context, "조건이 추가되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
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
        ruleEditViewModel.itemDeleteEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                findNavController().navigateSafe(directions.actionTriggerFragmentToTriggerDeleteConfirmDialog())
            }
        }

        binding.addBtn.setOnClickListener { findNavController().navigateSafe(directions.actionTriggerFragmentToTriggerEditFragment()) }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) { onStartGoBack() }
        binding.beforeBtn.setOnClickListener { onStartGoBack() }
        binding.nextBtn.setOnClickListener {
            if (ruleEditViewModel.triggerRecyclerItemList.value?.isEmpty() == true) {
                findNavController().navigateSafe(directions.actionTriggerFragmentToEmptyTriggerDialog())
            } else {
                findNavController().navigateSafe(directions.actionTriggerFragmentToActionFragment())
            }
        }

        return binding.root
    }

    private fun onStartGoBack() {
        with(ruleEditViewModel) {
            if (editingRule.value != originalRule) {
                val bottomSheetDialog = BottomSheetDialog(requireContext())
                bottomSheetDialog.setContentView(R.layout.dialog_cancel_confirm)
                bottomSheetDialog.show()

                if (isNewRule) {
                    bottomSheetDialog.findViewById<TextView>(R.id.title)!!
                        .text = getString(R.string.go_back_confirm_trigger_to_main_new_rule)
                } else {
                    bottomSheetDialog.findViewById<TextView>(R.id.title)!!
                        .text = getString(R.string.go_back_confirm_trigger_to_main_old_rule)
                }

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
    }

    private fun goBack() {
        if (ruleEditViewModel.isNewRule) {
            findNavController().navigateSafe(directions.actionTriggerFragmentToMainFragment())
        } else {
            findNavController().navigateSafe(directions.actionTriggerFragmentToDescriptionFragment())
        }
    }
}