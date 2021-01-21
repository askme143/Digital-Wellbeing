package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.DialogRingerBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe

class RingerDialog : DialogFragment() {
    private var _binding: DialogRingerBinding? = null
    private val binding get() = _binding!!

    private val directions = RingerDialogDirections

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogRingerBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionRingerDialogToActionEditFragment())
        }
        binding.completeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }

        return binding.root
    }
}