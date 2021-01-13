package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentActionBinding

class ActionFragment : Fragment() {
    private var _binding: FragmentActionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActionBinding.inflate(inflater, container, false)

        binding.addBtn.setOnClickListener { }
        binding.beforeBtn.setOnClickListener{ findNavController().navigate(R.id.action_actionFragment_to_triggerFragment) }
        binding.nextBtn.setOnClickListener { findNavController().navigate(R.id.action_actionFragment_to_confirmFragment) }

        return binding.root
    }
}