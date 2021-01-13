package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.DialogNotiKeywordBinding

class NotiKeywordDialog : DialogFragment() {
    private var _binding: DialogNotiKeywordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogNotiKeywordBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener { findNavController().navigate(R.id.action_notiKeywordDialog_to_notiEditFragment) }
        binding.completeBtn.setOnClickListener { findNavController().navigate(R.id.action_notiKeywordDialog_to_notiEditFragment) }

        return binding.root
    }
}