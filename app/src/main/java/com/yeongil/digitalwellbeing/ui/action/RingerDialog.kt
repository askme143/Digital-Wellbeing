package com.yeongil.digitalwellbeing.ui.action

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yeongil.digitalwellbeing.dataSource.ruleDatabase.RuleDatabase
import com.yeongil.digitalwellbeing.databinding.DialogRingerBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.RingerActionViewModel
import com.yeongil.digitalwellbeing.viewModel.RuleEditViewModel
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

        initViewModel()

        binding.cancelBtn.setOnClickListener {
            val editing = ruleEditViewModel.editingRule.value!!.ringerAction != null
            if (editing)
                findNavController().navigateSafe(directions.actionGlobalActionFragment())
            else
                findNavController().navigateSafe(directions.actionRingerDialogToActionEditFragment())
        }
        binding.completeBtn.setOnClickListener {
            ruleEditViewModel.addTriggerAction(ringerActionViewModel.getRingerAction())
            findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }

        return binding.root
    }

    private fun initViewModel() {
        val action = ruleEditViewModel.editingRule.value?.ringerAction

        if (action != null) {
            ringerActionViewModel.init(action)
        } else ringerActionViewModel.init()
    }
}