package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentAppListConfirmBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe

class AppListConfirmFragment : Fragment() {
    private var _binding: FragmentAppListConfirmBinding? = null
    private val binding get() = _binding!!

    private val directions = AppListConfirmFragmentDirections

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAppListConfirmBinding.inflate(inflater, container, false)

        binding.addBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionAppListConfirmFragmentToAppListFragment())
        }
        binding.beforeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionAppListConfirmFragmentToActionEditFragment())
        }
        binding.completeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionGlobalActionFragment())
        }

        return binding.root
    }
}