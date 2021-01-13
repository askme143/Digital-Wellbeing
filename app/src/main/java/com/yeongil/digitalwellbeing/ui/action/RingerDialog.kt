package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.DialogRingerBinding

class RingerDialog : DialogFragment() {
    private var _binding: DialogRingerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogRingerBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener { findNavController().navigate(R.id.action_ringerDialog_to_actionEditFragment) }
        binding.completeBtn.setOnClickListener { findNavController().navigate(R.id.action_global_actionFragment) }

        return binding.root
    }
}