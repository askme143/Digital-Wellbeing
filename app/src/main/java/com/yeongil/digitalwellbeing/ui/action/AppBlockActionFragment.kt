package com.yeongil.digitalwellbeing.ui.action

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yeongil.digitalwellbeing.databinding.FragmentAppBlockActionBinding
import com.yeongil.digitalwellbeing.repository.PackageManagerRepository
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.AppBlockActionViewModel
import com.yeongil.digitalwellbeing.viewModel.item.AppItem
import com.yeongil.digitalwellbeing.viewModelFactory.AppBlockActionViewModelFactory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AppBlockActionFragment : Fragment() {
    private var _binding: FragmentAppBlockActionBinding? = null
    private val binding get() = _binding!!

    private val directions = AppBlockActionFragmentDirections

    private val appBlockActionViewModel by activityViewModels<AppBlockActionViewModel> {
        AppBlockActionViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppBlockActionBinding.inflate(inflater, container, false)

        initViewModel()

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

    private fun initViewModel() {
        appBlockActionViewModel.init()
    }
}