package com.yeongil.focusaid.ui.trigger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeongil.focusaid.R
import com.yeongil.focusaid.databinding.FragmentTriggerEditBinding
import com.yeongil.focusaid.utils.navigateSafe

class TriggerEditFragment : Fragment() {
    private var _binding: FragmentTriggerEditBinding? = null
    private val binding get() = _binding!!

    private val directions = TriggerEditFragmentDirections

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTriggerEditBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener { findNavController().navigate(R.id.action_global_triggerFragment) }

        binding.locationBtn.setOnClickListener { findNavController().navigateSafe(directions.actionTriggerEditFragmentToLocationTriggerFragment()) }
        binding.timeBtn.setOnClickListener { findNavController().navigateSafe(directions.actionTriggerEditFragmentToTimeTriggerDialog()) }
        binding.activityBtn.setOnClickListener { findNavController().navigateSafe(directions.actionTriggerEditFragmentToActivityTriggerDialog()) }

        return binding.root
    }
}