package com.yeongil.digitalwellbeing.ui.trigger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.databinding.FragmentLocationTriggerBinding

class LocationTriggerFragment: Fragment(), OnMapReadyCallback {
    private var _binding: FragmentLocationTriggerBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationTriggerBinding.inflate(inflater, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.beforeBtn.setOnClickListener { findNavController().navigate(R.id.action_locationTriggerFragment_to_triggerEditFragment) }
        binding.completeBtn.setOnClickListener { findNavController().navigate(R.id.action_global_triggerFragment) }

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }
}