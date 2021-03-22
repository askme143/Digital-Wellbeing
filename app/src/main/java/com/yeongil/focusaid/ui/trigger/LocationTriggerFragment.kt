package com.yeongil.focusaid.ui.trigger

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.yeongil.focusaid.R
import com.yeongil.focusaid.data.rule.trigger.LocationTrigger
import com.yeongil.focusaid.databinding.FragmentLocationTriggerBinding
import com.yeongil.focusaid.utils.NetworkStatus
import com.yeongil.focusaid.utils.navigateSafe
import com.yeongil.focusaid.viewModel.viewModel.rule.RuleEditViewModel
import com.yeongil.focusaid.viewModel.viewModel.trigger.LocationSearchViewModel
import com.yeongil.focusaid.viewModel.viewModel.trigger.LocationTriggerViewModel
import com.yeongil.focusaid.viewModelFactory.LocationSearchViewModelFactory
import com.yeongil.focusaid.viewModelFactory.LocationTriggerViewModelFactory
import com.yeongil.focusaid.viewModelFactory.RuleEditViewModelFactory

class LocationTriggerFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentLocationTriggerBinding? = null
    private val binding get() = _binding!!

    private val directions = LocationTriggerFragmentDirections

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        RuleEditViewModelFactory(requireContext())
    }
    private val locationTriggerViewModel by activityViewModels<LocationTriggerViewModel> {
        LocationTriggerViewModelFactory()
    }
    private val locationSearchViewModel by activityViewModels<LocationSearchViewModel> {
        LocationSearchViewModelFactory()
    }

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val isNetworkAvailable by lazy { NetworkStatus.isNetworkAvailable(requireContext()) }

    private var marker: Marker? = null
    private var circle: Circle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationTriggerBinding.inflate(inflater, container, false)
        binding.vm = locationTriggerViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        checkPermission()

        return binding.root
    }

    private fun startGoogleMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.isMyLocationEnabled = true
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        initViewModel()
        setListeners()
    }

    @SuppressLint("MissingPermission")
    private fun initViewModel() {
        if (!locationTriggerViewModel.editing) {
            val trigger = ruleEditViewModel.editingRule.value?.locationTrigger
            if (trigger != null) {
                locationTriggerViewModel.init(trigger)
            } else {
                fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                    location?.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        locationTriggerViewModel.init(latLng)
                        locationSearchViewModel.init(latLng)
                    }
                }
            }
            locationTriggerViewModel.editing = true
        }
    }

    private fun setListeners() {
        if (isNetworkAvailable) {
            locationTriggerViewModel.latLng.observe(viewLifecycleOwner) {
                moveCamera(it)
                drawMarker(it)
            }
            locationTriggerViewModel.locationTrigger.observe(viewLifecycleOwner) {
                if (it != null) {
                    drawCircle(it)
                }
            }
            map.setOnMapLongClickListener {
                locationTriggerViewModel.latLng.value = it
                locationTriggerViewModel.doReverseGeocoding()
            }

            binding.searchBar.setOnClickListener {
                val keyword = locationTriggerViewModel.locationName.value!!
                locationSearchViewModel.setKeyword(keyword)
                findNavController().navigateSafe(directions.actionLocationTriggerFragmentToLocationSearchFragment())
            }
            binding.beforeBtn.setOnClickListener {
                locationTriggerViewModel.editing = false
                val goToEditFragment = ruleEditViewModel.editingRule.value?.locationTrigger == null
                if (goToEditFragment)
                    findNavController().navigateSafe(directions.actionLocationTriggerFragmentToTriggerEditFragment())
                else
                    findNavController().navigateSafe(directions.actionGlobalTriggerFragment())
            }
            binding.completeBtn.setOnClickListener {
                locationTriggerViewModel.editing = false
                locationTriggerViewModel.locationTrigger.value?.let {
                    ruleEditViewModel.addTriggerAction(it)
                }
                findNavController().navigateSafe(directions.actionGlobalTriggerFragment())
            }
        } else {
            binding.beforeBtn.setOnClickListener {
                locationTriggerViewModel.editing = false
                val goToEditFragment = ruleEditViewModel.editingRule.value?.locationTrigger == null
                if (goToEditFragment)
                    findNavController().navigateSafe(directions.actionLocationTriggerFragmentToTriggerEditFragment())
                else
                    findNavController().navigateSafe(directions.actionGlobalTriggerFragment())
            }
            map.setOnMapLongClickListener {
                Toast.makeText(requireContext(), "연결된 인터넷이 없습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun moveCamera(latLng: LatLng) {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16F)

        locationTriggerViewModel.cameraMoveFast.getContentIfNotHandled()
            ?.let { map.moveCamera(cameraUpdate) }
            ?: map.animateCamera(cameraUpdate)
    }

    private fun drawMarker(latLng: LatLng) {
        val newMarker = MarkerOptions().position(latLng)

        marker?.remove()
        marker = map.addMarker(newMarker)
    }

    private fun drawCircle(locationTrigger: LocationTrigger) {
        val latLng = LatLng(locationTrigger.latitude, locationTrigger.longitude)
        val range = locationTrigger.range
        val newCircle = CircleOptions()
            .center(latLng)
            .radius(range.toDouble())
            .fillColor(0x30ff0000)
            .strokeWidth(0f)

        circle?.remove()
        circle = map.addCircle(newCircle)
    }

    private fun checkPermission() {
        if (
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startGoogleMap()
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(
                    context, "위치 설정을 위해서는 권한을 설정해야 합니다",
                    Toast.LENGTH_LONG
                ).show()
            }
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
            ) {
                startGoogleMap()
            }
        }
    }
}