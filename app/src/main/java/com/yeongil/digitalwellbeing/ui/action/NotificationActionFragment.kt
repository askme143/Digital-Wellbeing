package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.databinding.FragmentNotificationActionBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.NotificationActionViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.NotificationActionViewModelFactory

class NotificationActionFragment : Fragment() {
    private var _binding: FragmentNotificationActionBinding? = null
    private val binding get() = _binding!!

    private val directions = NotificationActionFragmentDirections

    private val notificationActionViewModel by activityViewModels<NotificationActionViewModel> {
        NotificationActionViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationActionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = notificationActionViewModel

        // View TODOs
        // TODO: Make item_keyword

        // NotificationActionViewModel TODOs
        // TODO: Initialize View Model
        // TODO: getNotificationAction
        // TODO: set Apps
        // TODO: add Keywords (+ update)

        // Click Listener TODOs
        // TODO: App Item Delete Click - Delete a selected App
        // TODO: Keyword Item Click - Pass the clicked keyword and navigate to a keyword dialog
        // TODO: Keyword Item Delete Click - Delete a selected keyword

        binding.addAppBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionNotificationActionFragmentToNotiAppListFragment())
        }
        binding.addKeywordBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionNotificationActionFragmentToNotiKeywordDialog())
        }
        binding.beforeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionNotificationActionFragmentToActionEditFragment())
        }
        binding.completeBtn.setOnClickListener {
            // TODO: Add notification action to the RuleEditViewModel
            findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }

        return binding.root
    }
}