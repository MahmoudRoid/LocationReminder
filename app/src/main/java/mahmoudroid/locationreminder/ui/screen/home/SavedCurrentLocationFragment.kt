package mahmoudroid.locationreminder.ui.screen.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import mahmoudroid.locationreminder.R
import mahmoudroid.locationreminder.databinding.FragmentSavedCurrentLocationBinding
import mahmoudroid.locationreminder.ui.base.BaseFragment
import mahmoudroid.locationreminder.util.LocationUtils
import mahmoudroid.locationreminder.util.PermissionUtils
import mahmoudroid.locationreminder.workmanager.LocationWorker
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SavedCurrentLocationFragment: BaseFragment() {

    private var _binding: FragmentSavedCurrentLocationBinding? = null
    private val binding get() = _binding!!
    //private val viewModel: SavedCurrentLocationFragmentVM by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_REQ_CODE = 10005
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedCurrentLocationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        locationCallback = object: LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult?.let {
                    for (location in locationResult.locations){
                        location?.let {
                            Log.i("TAG", "onLocationResult: ${location.latitude.toString() + "//" + location.longitude.toString()}")
                            showToast(location.latitude.toString() + "//" + location.longitude.toString())
                            //                                calcul(it)
                        }
                    }
                }
            }
        }


        init()

    }



    @SuppressLint("UnspecifiedImmutableFlag")
    private fun init(){
        binding.saveBtn.setOnClickListener {
            navigateTo( HomeFragmentDirections.actionHomeFragmentToMapFragment())
        }
        binding.getLocationBtn.setOnClickListener { getCurrentLocation() }
    }

    @SuppressLint("MissingPermission")
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

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 5000

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

        }


    }

/*    private fun calcul(newLocation: Location) {
        val location: Location = Location("").apply {
            latitude = 35.7609067
            longitude = 51.4036601
        }
        if (LocationUtils.distanceInMeter(location,newLocation) > 10){
            showRegularNotification()
        }
    }*/


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
        if (requestCode == LOCATION_REQ_CODE) {
            getCurrentLocation()
        }
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}