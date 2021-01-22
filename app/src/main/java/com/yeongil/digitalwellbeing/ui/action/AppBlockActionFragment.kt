package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.databinding.FragmentAppBlockActionBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe

class AppBlockActionFragment : Fragment() {
    private var _binding: FragmentAppBlockActionBinding? = null
    private val binding get() = _binding!!

    private val directions = AppBlockActionFragmentDirections

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppBlockActionBinding.inflate(inflater, container, false)

        binding.addBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionAppBlockActionFragmentToAppBlockListFragment())
        }
        binding.beforeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionAppBlockActionFragmentToActionEditFragment())
        }
        binding.completeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }

        return binding.root
    }
}