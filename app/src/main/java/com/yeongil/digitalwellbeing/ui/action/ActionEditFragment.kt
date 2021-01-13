package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentActionEditBinding

class ActionEditFragment : Fragment() {
    private var _binding: FragmentActionEditBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActionEditBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener { findNavController().navigate(R.id.action_global_actionFragment) }

        binding.appTimeBtn.setOnClickListener { findNavController().navigate(R.id.action_actionEditFragment_to_appListConfirmFragment) }
        binding.notificationBtn.setOnClickListener { findNavController().navigate(R.id.action_actionEditFragment_to_notiEditFragment) }
        binding.dndBtn.setOnClickListener { findNavController().navigate(R.id.action_actionEditFragment_to_dndDialog) }
        binding.ringerBtn.setOnClickListener { findNavController().navigate(R.id.action_actionEditFragment_to_ringerDialog) }

        return binding.root
    }
}