package com.yeongil.digitalwellbeing.ui.action

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentActionBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe

class ActionFragment : Fragment() {
    private var _binding: FragmentActionBinding? = null
    private val binding get() = _binding!!

    private val directions = ActionFragmentDirections

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActionBinding.inflate(inflater, container, false)

        binding.addBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionActionFragmentToActionNestedGraph())
        }
        binding.beforeBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionActionFragmentToTriggerFragment())
        }
        binding.nextBtn.setOnClickListener {
            findNavController().navigateSafe(directions.actionActionFragmentToConfirmFragment())
        }

        return binding.root
    }
}