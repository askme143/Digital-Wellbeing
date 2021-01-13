package com.yeongil.digitalwellbeing.ui.trigger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentTriggerEditBinding

class TriggerEditFragment : Fragment() {
    private var _binding: FragmentTriggerEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTriggerEditBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener { findNavController().navigate(R.id.action_global_triggerFragment) }
        
        binding.locationBtn.setOnClickListener { findNavController().navigate(R.id.action_triggerEditFragment_to_locationTriggerFragment) }
        binding.timeBtn.setOnClickListener { findNavController().navigate(R.id.action_triggerEditFragment_to_timeTriggerDialog) }
        binding.activityBtn.setOnClickListener { findNavController().navigate(R.id.action_triggerEditFragment_to_activityTriggerDialog) }

        return binding.root
    }
}