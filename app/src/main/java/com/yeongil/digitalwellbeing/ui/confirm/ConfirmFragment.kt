package com.yeongil.digitalwellbeing.ui.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentConfirmBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe

class ConfirmFragment : Fragment() {
    private var _binding: FragmentConfirmBinding? = null
    private val binding get() = _binding!!

    private val directions = ConfirmFragmentDirections

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfirmBinding.inflate(inflater, container, false)

        binding.addBtn.setOnClickListener { }
        binding.beforeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionConfirmFragmentToActionFragment())
        }
        binding.nextBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionConfirmFragmentToConfirmDialog())
        }

        return binding.root
    }
}