package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentAppListConfirmBinding

class AppListConfirmFragment : Fragment() {
    private var _binding: FragmentAppListConfirmBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAppListConfirmBinding.inflate(inflater, container, false)

        binding.addBtn.setOnClickListener { findNavController().navigate(R.id.action_appListConfirmFragment_to_appListFragment) }
        binding.beforeBtn.setOnClickListener { findNavController().navigate(R.id.action_appListConfirmFragment_to_actionEditFragment) }
        binding.completeBtn.setOnClickListener { findNavController().navigate(R.id.action_global_actionFragment) }

        return binding.root
    }
}