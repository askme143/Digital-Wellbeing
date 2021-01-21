package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentAppListBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe

class AppListFragment : Fragment() {
    private var _binding: FragmentAppListBinding? = null
    private val binding get() = _binding!!

    private val directions = AppListFragmentDirections

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAppListBinding.inflate(inflater, container, false)

        binding.beforeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionAppListFragmentToAppListConfirmFragment())
        }
        binding.completeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionAppListFragmentToAppListConfirmFragment())
        }

        return binding.root
    }
}