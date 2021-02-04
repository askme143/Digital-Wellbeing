package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.databinding.FragmentActionBinding
import com.yeongil.digitalwellbeing.utils.*
import com.yeongil.digitalwellbeing.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModel.item.APP_BLOCK_ACTION_TITLE
import com.yeongil.digitalwellbeing.viewModel.item.DND_ACTION_TITLE
import com.yeongil.digitalwellbeing.viewModel.item.NOTIFICATION_ACTION_TITLE
import com.yeongil.digitalwellbeing.viewModel.item.RINGER_ACTION_TITLE
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.AppBlockActionViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.action.NotificationActionViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.AppBlockActionViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.NotificationActionViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

class ActionFragment : Fragment() {
    private var _binding: FragmentActionBinding? = null
    private val binding get() = _binding!!

    private val directions = ActionFragmentDirections

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }
    private val appBlockActionViewModel by activityViewModels<AppBlockActionViewModel> {
        AppBlockActionViewModelFactory(requireContext())
    }
    private val notificationAcViewModel by activityViewModels<NotificationActionViewModel> {
        NotificationActionViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = ruleEditViewModel

        ruleEditViewModel.itemAddEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(context, "액션이 추가되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        ruleEditViewModel.itemEditEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                Toast.makeText(context, "액션이 수정되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        ruleEditViewModel.itemClickEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { title ->
                when (title) {
                    APP_BLOCK_ACTION_TITLE -> {
                        appBlockActionViewModel.putAppBlockAction(ruleEditViewModel.editingRule.value!!.appBlockAction)
                        findNavController().navigateSafe(directions.actionActionFragmentToAppBlockActionFragment())
                    }
                    NOTIFICATION_ACTION_TITLE -> {
                        notificationAcViewModel.putNotificationAction(ruleEditViewModel.editingRule.value!!.notificationAction)
                        findNavController().navigateSafe(directions.actionActionFragmentToNotificationActionFragment())
                    }
                    RINGER_ACTION_TITLE ->
                        findNavController().navigateSafe(directions.actionActionFragmentToRingerDialog())
                }
            }
        }
        ruleEditViewModel.itemDeleteEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                findNavController().navigateSafe(directions.actionActionFragmentToActionDeleteConfirmDialog())
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