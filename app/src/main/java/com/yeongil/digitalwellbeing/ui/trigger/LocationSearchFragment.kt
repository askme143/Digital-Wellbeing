package com.yeongil.digitalwellbeing.ui.trigger

import android.app.Service
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.yeongil.digitalwellbeing.databinding.FragmentLocationSearchBinding

class LocationSearchFragment : Fragment() {
    private var _binding: FragmentLocationSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationSearchBinding.inflate(inflater, container, false)

        // TODO: Make View Model
        binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == KeyEvent.KEYCODE_HOME) {
                search()
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

    private fun search() {
        // TODO: Do Search
    }
}