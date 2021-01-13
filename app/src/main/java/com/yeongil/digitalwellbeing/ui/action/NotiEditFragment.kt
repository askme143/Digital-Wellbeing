package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentNotiEditBinding

class NotiEditFragment : Fragment() {
    private var _binding: FragmentNotiEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotiEditBinding.inflate(inflater, container, false)

        binding.addAppBtn.setOnClickListener { findNavController().navigate(R.id.action_notiEditFragment_to_notiAppListFragment) }
        binding.addKeywordBtn.setOnClickListener { findNavController().navigate(R.id.action_notiEditFragment_to_notiKeywordDialog) }
        binding.beforeBtn.setOnClickListener { findNavController().navigate(R.id.action_notiEditFragment_to_actionEditFragment) }
        binding.completeBtn.setOnClickListener { findNavController().navigate(R.id.action_global_actionFragment) }

        return binding.root
    }
}