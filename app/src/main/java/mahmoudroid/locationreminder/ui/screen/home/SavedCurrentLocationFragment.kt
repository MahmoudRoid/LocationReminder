package mahmoudroid.locationreminder.ui.screen.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import mahmoudroid.locationreminder.R
import mahmoudroid.locationreminder.databinding.FragmentSavedCurrentLocationBinding
import mahmoudroid.locationreminder.ui.base.BaseFragment
import mahmoudroid.locationreminder.util.LocationUtils
import mahmoudroid.locationreminder.util.PermissionUtils

@AndroidEntryPoint
class SavedCurrentLocationFragment: BaseFragment() {

    private var _binding: FragmentSavedCurrentLocationBinding? = null
    private val binding get() = _binding!!
    //private val viewModel: SavedCurrentLocationFragmentVM by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_REQ_CODE = 10005
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
        init()
        update()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun init(){
        binding.getLocationBtn.setOnClickListener { getCurrentLocation() }
/*        binding.getLocationBtn.setOnClickListener {
            NotificationUtils.showRegularNotification(
                context = requireContext(),
                title = "hello",
                message = "hello every one, please pay attention, you are in a way you shall not",
                pendingIntent = PendingIntent.getActivity(
                    requireActivity(),
                    nextInt(0,9999)
                    ,Intent(requireActivity(), MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    } ,0)
            )
        }*/
    }


    private fun update(){
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations){
                 //update ur msg
                    showToast(location.longitude.toString() + "//" + location.longitude.toString())
                }
            }
        }
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
            fusedLocationClient.lastLocation.addOnSuccessListener { loc ->
                if (loc != null)
                    showToast(loc.latitude.toString() + "//" + loc.longitude.toString())
                else
                    showToast("null" + "")

            }
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
        if (requestCode == LOCATION_REQ_CODE) {
            getCurrentLocation()
        }
    }




    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}