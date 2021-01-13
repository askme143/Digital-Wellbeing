package com.yeongil.digitalwellbeing.ui.trigger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentTriggerBinding

class TriggerFragment : Fragment() {
    private var _binding: FragmentTriggerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTriggerBinding.inflate(inflater, container, false)

        binding.addBtn.setOnClickListener {findNavController().navigate(R.id.action_triggerFragment_to_triggerNestedGraph) }
        binding.beforeBtn.setOnClickListener{ findNavController().navigate(R.id.action_triggerFragment_to_mainFragment) }
        binding.nextBtn.setOnClickListener { findNavController().navigate(R.id.action_triggerFragment_to_actionFragment) }

        return binding.root
    }
}