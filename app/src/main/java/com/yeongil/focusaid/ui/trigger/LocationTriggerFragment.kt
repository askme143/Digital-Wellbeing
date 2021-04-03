package com.yeongil.focusaid.ui.trigger

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.yeongil.focusaid.R
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
        LocationTriggerViewModelFactory(requireContext())
    }
    private val locationSearchViewModel by activityViewModels<LocationSearchViewModel> {
        LocationSearchViewModelFactory(requireContext())
    }

    private lateinit var map: GoogleMap

    private var marker: Marker? = null
    private var circle: Circle? = null
    private var firstCameraMove: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationTriggerBinding.inflate(inflater, container, false)
        binding.vm = locationTriggerViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        if (checkPermission()) {
            initLocationFragment()
        } else {
            requestPermission()
        }

        return binding.root
    }

    private fun initLocationFragment() {
        val fromSearchFragment =
            locationTriggerViewModel.fromSearchFragment.getContentIfNotHandled() ?: false

        if (!fromSearchFragment) {
            locationTriggerViewModel.putLocationTrigger(
                ruleEditViewModel.editingRule.value?.locationTrigger
            )
        }

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        if (!checkPermission()) return

        map = googleMap
        map.isMyLocationEnabled = true
        setListeners()
    }

    private fun setListeners() {
        if (checkNetwork()) {
            locationTriggerViewModel.latLng.observe(viewLifecycleOwner) {
                /* Latitude or longitude changed. */
                val noAnimation =
                    firstCameraMove ||
                            locationTriggerViewModel.noAnimation.getContentIfNotHandled() ?: false
                firstCameraMove = false

                moveCamera(it, noAnimation)
                drawMarker(it)
            }
            locationTriggerViewModel.locationTrigger.observe(viewLifecycleOwner) {
                /* Latitude or longitude or range changed. */
                if (it != null) drawCircle(LatLng(it.latitude, it.longitude), it.range)
            }
            locationTriggerViewModel.internetErrorEvent.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let {
                    Toast.makeText(context, "인터넷 연결을 확인하세요.", Toast.LENGTH_SHORT).show()
                }
            }

            map.setOnMapLongClickListener {
                /* Change coordinate to a long clicked position */
                locationTriggerViewModel.updateLatLng(it)
            }
            binding.searchBar.setOnClickListener {
                val keyword = locationTriggerViewModel.locationName.value!!
                locationSearchViewModel.setKeyword(keyword)
                findNavController().navigateSafe(directions.actionLocationTriggerFragmentToLocationSearchFragment())
            }
            binding.completeBtn.isEnabled = true
        } else {
            binding.completeBtn.isEnabled = false

            binding.beforeBtn.setOnClickListener {
                val goToEditFragment = ruleEditViewModel.editingRule.value?.locationTrigger == null
                if (goToEditFragment)
                    findNavController().navigateSafe(directions.actionLocationTriggerFragmentToTriggerEditFragment())
                else
                    findNavController().navigateSafe(directions.actionGlobalTriggerFragment())
            }
            map.setOnMapLongClickListener {
                if (checkNetwork()) setListeners()
                else Toast.makeText(requireContext(), "인터넷 연결을 확인하세요.", Toast.LENGTH_LONG).show()
            }

            Toast.makeText(requireContext(), "인터넷 연결을 확인하세요.", Toast.LENGTH_LONG).show()
        }

        binding.beforeBtn.setOnClickListener {
            val goToEditFragment = ruleEditViewModel.editingRule.value?.locationTrigger == null
            if (goToEditFragment)
                findNavController().navigateSafe(directions.actionLocationTriggerFragmentToTriggerEditFragment())
            else
                findNavController().navigateSafe(directions.actionGlobalTriggerFragment())
        }
        binding.completeBtn.setOnClickListener {
            locationTriggerViewModel.locationTrigger.value?.let {
                ruleEditViewModel.addTriggerAction(it)
            }
            findNavController().navigateSafe(directions.actionGlobalTriggerFragment())
        }
    }

    private fun moveCamera(latLng: LatLng, noAnimation: Boolean) {
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16F)

        if (noAnimation) map.moveCamera(cameraUpdate)
        else map.animateCamera(cameraUpdate)
    }

    private fun drawMarker(latLng: LatLng) {
        val newMarker = MarkerOptions().position(latLng)

        marker?.remove()
        marker = map.addMarker(newMarker)
    }

    private fun drawCircle(latLng: LatLng, range: Int) {
        val newCircle = CircleOptions()
            .center(latLng)
            .radius(range.toDouble())
            .fillColor(0x30ff0000)
            .strokeWidth(0f)

        circle?.remove()
        circle = map.addCircle(newCircle)
    }

    private fun checkNetwork(): Boolean {
        return NetworkStatus.isNetworkAvailable(requireContext())
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(
                context, "위치 설정을 위해서는 권한을 설정해야 합니다",
                Toast.LENGTH_LONG
            ).show()
        }
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
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
                initLocationFragment()
            } else {
                Toast.makeText(
                    context, "위치 설정을 위해서는 권한을 설정해야 합니다",
                    Toast.LENGTH_LONG
                ).show()

                val goToEditFragment = ruleEditViewModel.editingRule.value?.locationTrigger == null
                if (goToEditFragment)
                    findNavController().navigateSafe(directions.actionLocationTriggerFragmentToTriggerEditFragment())
                else
                    findNavController().navigateSafe(directions.actionGlobalTriggerFragment())
            }
        }
    }
}