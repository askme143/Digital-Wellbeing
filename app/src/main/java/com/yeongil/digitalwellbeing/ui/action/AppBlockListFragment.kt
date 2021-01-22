package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentAppBlockListBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe

class AppBlockListFragment : Fragment() {
    private var _binding: FragmentAppBlockListBinding? = null
    private val binding get() = _binding!!

    private val directions = AppBlockListFragmentDirections

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAppBlockListBinding.inflate(inflater, container, false)

        binding.beforeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionAppBlockListFragmentToAppBlockActionFragment())
        }
        binding.completeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionAppBlockListFragmentToAppBlockActionFragment())
        }

        return binding.root
    }
}