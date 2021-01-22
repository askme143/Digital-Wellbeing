package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.databinding.FragmentNotificationActionBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe

class NotificationActionFragment : Fragment() {
    private var _binding: FragmentNotificationActionBinding? = null
    private val binding get() = _binding!!

    private val directions = NotificationActionFragmentDirections

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationActionBinding.inflate(inflater, container, false)

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
            findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }

        return binding.root
    }
}