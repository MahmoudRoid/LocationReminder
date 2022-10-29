package mahmoudroid.locationreminder.ui.screen.map

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.Marker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mahmoudroid.locationreminder.R
import mahmoudroid.locationreminder.data.Constants.MAP_LOCATION
import mahmoudroid.locationreminder.data.LocationRepository
import mahmoudroid.locationreminder.databinding.FragmentMapBinding
import mahmoudroid.locationreminder.ext.setNavigationResult
import mahmoudroid.locationreminder.ui.base.BaseFragment
import mahmoudroid.locationreminder.ui.base.BaseMapView
import mahmoudroid.locationreminder.util.LocationUtils
import mahmoudroid.locationreminder.util.PermissionUtils
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment: BaseFragment(), BaseMapView.IMapViewCallBacks{

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private var currentLocation: Location? = null
    @Inject lateinit var locationRepository: LocationRepository
    private val LOCATION_REQ_CODE = 10005

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initObserver()
        getCurrentLocation()
    }

    private fun initObserver(){
        lifecycleScope.launch{
            locationRepository.lastLocation.collect{ location ->
                location?.let {
                    currentLocation = location
                }
            }
        }
    }

    private fun initUI() {
        binding.mapView.setOnMapReadyListener(this)

        binding.currentLocationBtn.setOnClickListener {
            if (PermissionUtils.hasLocationPermission(requireContext())) {
                moveToCurrentLocation()
            } else {
                requestLocationPermission()
            }
        }

        binding.chooseLocationBtnMap.setOnClickListener {
            setNavigationResult(MAP_LOCATION, binding.mapView.getMapCenter().toString())
            onBackPressed()
        }
    }

    private fun getCurrentLocation(){
        if (PermissionUtils.hasLocationPermission(requireContext()).not()){
            requestLocationPermission()
            return
        }
        if (LocationUtils.isGpsEnables(requireActivity()).not()){
            LocationUtils.requestToTurnOnGps(requireActivity(), LOCATION_REQ_CODE)
            return
        }
        else{
            locationRepository.startLocationUpdates()
            binding.mapView.setMyLocationEnable()
        }
    }

    private fun requestLocationPermission() {
        PermissionUtils.requestForLocationPermission(requireActivity(),object : PermissionUtils.PermissionUtilsListener{
            override fun onGranted() {
                getCurrentLocation()
            }

            override fun onDenied() {
                showToast(getString(R.string.accept_locations_permission))
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == LOCATION_REQ_CODE)
            getCurrentLocation()
    }

    private fun moveToCurrentLocation() {
        if (currentLocation != null) {
            binding.mapView.moveToCurrentLocation(currentLocation!!.latitude, currentLocation!!.longitude)
        }
    }

    override fun mapIsReady() {
        if (PermissionUtils.hasLocationPermission(requireContext()))
            if (currentLocation != null)
                moveToCurrentLocation()
    }

    override fun onCameraMove() {}

    override fun onMarkerClick(marker: Marker) {}

    override fun onDestroyView() {
        locationRepository.stopLocationUpdates()
        _binding = null
        super.onDestroyView()
    }

}