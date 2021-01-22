package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.databinding.DialogNotiKeywordBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe

class NotiKeywordDialog : DialogFragment() {
    private var _binding: DialogNotiKeywordBinding? = null
    private val binding get() = _binding!!

    private val directions = NotiKeywordDialogDirections

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogNotiKeywordBinding.inflate(inflater, container, false)

        binding.cancelBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionNotiKeywordDialogToNotificationActionFragment())
        }
        binding.completeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionNotiKeywordDialogToNotificationActionFragment())
        }

        return binding.root
    }
}