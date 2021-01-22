package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.databinding.FragmentActionEditBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe

class ActionEditFragment : Fragment() {
    private var _binding: FragmentActionEditBinding? = null
    private val binding get() = _binding!!

    private val directions = ActionEditFragmentDirections

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActionEditBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener { findNavController().navigateSafe(directions.actionGlobalActionFragment()) }

        binding.appTimeBtn.setOnClickListener { findNavController().navigateSafe(directions.actionActionEditFragmentToAppBlockActionFragment()) }
        binding.notificationBtn.setOnClickListener { findNavController().navigateSafe(directions.actionActionEditFragmentToNotificationActionFragment()) }
        binding.dndBtn.setOnClickListener { findNavController().navigateSafe(directions.actionActionEditFragmentToDndDialog()) }
        binding.ringerBtn.setOnClickListener { findNavController().navigateSafe(directions.actionActionEditFragmentToRingerDialog()) }

        return binding.root
    }
}