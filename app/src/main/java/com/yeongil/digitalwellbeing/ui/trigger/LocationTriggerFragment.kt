package com.yeongil.digitalwellbeing.ui.trigger

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
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
import com.yeongil.digitalwellbeing.R
import com.yeongil.digitalwellbeing.database.ruleDatabase.RuleDatabase
import com.yeongil.digitalwellbeing.database.ruleDatabase.dto.trigger.LocationTrigger
import com.yeongil.digitalwellbeing.databinding.FragmentLocationTriggerBinding
import com.yeongil.digitalwellbeing.utils.NetworkStatus
import com.yeongil.digitalwellbeing.utils.navigateSafe
import com.yeongil.digitalwellbeing.viewModel.RuleEditViewModel
import com.yeongil.digitalwellbeing.viewModel.LocationTriggerViewModel
import com.yeongil.digitalwellbeing.viewModelFactory.RuleEditViewModelFactory

class LocationTriggerFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentLocationTriggerBinding? = null
    private val binding get() = _binding!!

    private val directions = LocationTriggerFragmentDirections
    private val editing by lazy {
        ruleEditViewModel.editingRule.value?.locationTrigger != null
    }

    private val ruleEditViewModel by activityViewModels<RuleEditViewModel> {
        val ruleDao = RuleDatabase.getInstance(requireContext().applicationContext).ruleDao()
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        RuleEditViewModelFactory(ruleDao, sharedPref)
    }
    private val locationTriggerViewModel by activityViewModels<LocationTriggerViewModel>()

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
        val rid = ruleEditViewModel.editingRule.value!!.ruleInfo.rid
        val trigger = ruleEditViewModel.editingRule.value?.locationTrigger

        if (trigger != null) {
            Log.e("hello", "there is a pre-existing trigger")
            locationTriggerViewModel.init(trigger)
        } else {
            Log.e("hello", "there is no pre-existing trigger")
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    locationTriggerViewModel.init(LatLng(it.latitude, it.longitude), rid)
                }
            }
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
            }

            binding.beforeBtn.setOnClickListener {
                if (editing)
                    findNavController().navigateSafe(directions.actionGlobalTriggerFragment())
                else
                    findNavController().navigateSafe(directions.actionLocationTriggerFragmentToTriggerEditFragment())
            }
            binding.completeBtn.setOnClickListener {
                locationTriggerViewModel.locationTrigger.value?.let {
                    ruleEditViewModel.addLocationTrigger(it)
                }
                findNavController().navigateSafe(directions.actionGlobalTriggerFragment())
            }
        } else {
            map.setOnMapLongClickListener {
                Toast.makeText(requireContext(), "연결된 인터넷이 없습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun moveCamera(latLng: LatLng) {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16F)
        map.moveCamera(cameraUpdate)
    }

    private fun drawMarker(latLng: LatLng) {
        val newMarker = MarkerOptions().position(latLng)

        marker?.remove()
        marker = map.addMarker(newMarker)
    }

    private fun drawCircle(locationTrigger: LocationTrigger) {
        val latLng = LatLng(locationTrigger.latitude, locationTrigger.longitude)
        val range = locationTrigger.range
        val newCircle = CircleOptions().center(latLng).radius(range.toDouble())

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