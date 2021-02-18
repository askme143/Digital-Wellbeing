package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.digitalwellbeing.databinding.DialogRingerBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.RingerActionViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

class RingerDialog : BottomSheetDialogFragment() {
    private var _binding: DialogRingerBinding? = null
    private val binding get() = _binding!!

    private val directions = RingerDialogDirections

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }
    private val ringerActionViewModel by activityViewModels<RingerActionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRingerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = ringerActionViewModel

        binding.cancelBtn.setOnClickListener {
            val goToEditFragment = ruleEditViewModel.editingRule.value!!.ringerAction == null
            if (goToEditFragment)
                findNavController().navigateSafe(directions.actionRingerDialogToActionEditFragment())
            else
                findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }
        binding.completeBtn.setOnClickListener {
            ruleEditViewModel.addTriggerAction(ringerActionViewModel.getRingerAction())
            findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }

        return binding.root
    }
}