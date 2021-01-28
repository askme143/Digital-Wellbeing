package com.yeongil.digitalwellbeing.ui.trigger

import android.app.Service
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.yeongil.digitalwellbeing.databinding.FragmentLocationSearchBinding
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.viewModel.trigger.LocationSearchViewModel
import com.yeongil.digitalwellbeing.viewModel.viewModel.trigger.LocationTriggerViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.LocationSearchViewModelFactory
import com.yeongil.digitalwellbeing.viewModelFactory.LocationTriggerViewModelFactory


class LocationSearchFragment : Fragment() {
    private var _binding: FragmentLocationSearchBinding? = null
    private val binding get() = _binding!!

    private val directions = LocationSearchFragmentDirections

    private val locationTriggerViewModel by activityViewModels<LocationTriggerViewModel> {
        LocationTriggerViewModelFactory()
    }
    private val locationSearchViewModel by activityViewModels<LocationSearchViewModel> {
        LocationSearchViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationSearchBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = locationSearchViewModel

        binding.resultRecyclerView.addItemDecoration(DividerItemDecoration(context, 1))

        locationSearchViewModel.itemClickEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                locationTriggerViewModel.submitSearchResult(it)
                findNavController().navigateSafe(directions.actionLocationSearchFragmentToLocationTriggerFragment())
            }
        }
        binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == KeyEvent.KEYCODE_HOME) {
                locationSearchViewModel.search()
                false
            } else true
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.searchBar.requestFocus()
        binding.searchBar.selectAll()
        val inputMethodManager =
            requireContext().getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.searchBar, 0)
    }
}