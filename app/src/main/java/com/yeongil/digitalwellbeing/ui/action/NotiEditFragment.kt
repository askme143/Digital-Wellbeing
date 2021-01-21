package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentNotiEditBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe

class NotiEditFragment : Fragment() {
    private var _binding: FragmentNotiEditBinding? = null
    private val binding get() = _binding!!

    private val directions = NotiEditFragmentDirections

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotiEditBinding.inflate(inflater, container, false)

        binding.addAppBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionNotiEditFragmentToNotiAppListFragment())
        }
        binding.addKeywordBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionNotiEditFragmentToNotiKeywordDialog())
        }
        binding.beforeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionNotiEditFragmentToActionEditFragment())
        }
        binding.completeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }

        return binding.root
    }
}