package com.yeongil.digitalwellbeing.ui.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentConfirmBinding

class ConfirmFragment : Fragment() {
    private var _binding: FragmentConfirmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfirmBinding.inflate(inflater, container, false)

        binding.addBtn.setOnClickListener { }
        binding.beforeBtn.setOnClickListener{ findNavController().navigate(R.id.action_confirmFragment_to_actionFragment) }
        binding.nextBtn.setOnClickListener { findNavController().navigate(R.id.action_confirmFragment_to_confirmDialog) }

        return binding.root
    }
}