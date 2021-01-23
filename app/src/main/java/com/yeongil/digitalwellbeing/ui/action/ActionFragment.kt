package com.yeongil.digitalwellbeing.ui.action

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.database.ruleDatabase.RuleDatabase
import com.yeongil.digitalwellbeing.databinding.FragmentActionBinding
import com.yeongil.digitalwellbeing.utils.*
import com.yeongil.digitalwellbeing.viewModel.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

class ActionFragment : Fragment() {
    private var _binding: FragmentActionBinding? = null
    private val binding get() = _binding!!

    private val directions = ActionFragmentDirections

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
        _binding = FragmentActionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = ruleEditViewModel

        ruleEditViewModel.itemClickEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                when (it) {
                    APP_BLOCK_ACTION_TITLE ->
                        findNavController().navigateSafe(directions.actionActionFragmentToAppBlockActionFragment())
                    NOTIFICATION_ACTION_TITLE ->
                        findNavController().navigateSafe(directions.actionActionFragmentToNotificationActionFragment())
                    DND_ACTION_TITLE ->
                        findNavController().navigateSafe(directions.actionActionFragmentToDndDialog())
                    RINGER_ACTION_TITLE ->
                        findNavController().navigateSafe(directions.actionActionFragmentToRingerDialog())
                }
            }
        }
        binding.addBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionActionFragmentToActionEditFragment())
        }
        binding.beforeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionActionFragmentToTriggerFragment())
        }
        binding.nextBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionActionFragmentToConfirmFragment())
        }

        return binding.root
    }
}