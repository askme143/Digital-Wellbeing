package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentNotiAppListBinding

class NotiAppListFragment : Fragment() {
    private var _binding: FragmentNotiAppListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotiAppListBinding.inflate(inflater, container, false)

        binding.beforeBtn.setOnClickListener { findNavController().navigate(R.id.action_notiAppListFragment_to_notiEditFragment) }
        binding.completeBtn.setOnClickListener { findNavController().navigate(R.id.action_notiAppListFragment_to_notiEditFragment) }

        return binding.root
    }
}