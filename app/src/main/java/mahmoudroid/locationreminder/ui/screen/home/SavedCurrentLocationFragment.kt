package mahmoudroid.locationreminder.ui.screen.home

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import mahmoudroid.locationreminder.R
import mahmoudroid.locationreminder.data.Constants
import mahmoudroid.locationreminder.data.NotificationModel
import mahmoudroid.locationreminder.databinding.FragmentSavedCurrentLocationBinding
import mahmoudroid.locationreminder.ext.getNavigationResult
import mahmoudroid.locationreminder.service.ForegroundLocationService
import mahmoudroid.locationreminder.ui.MainActivity
import mahmoudroid.locationreminder.ui.base.BaseFragment
import mahmoudroid.locationreminder.util.LocationUtils
import mahmoudroid.locationreminder.util.PermissionUtils
import kotlin.random.Random

@AndroidEntryPoint
class SavedCurrentLocationFragment: BaseFragment() {

    private var _binding: FragmentSavedCurrentLocationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SavedCurrentLocationFragmentVM by viewModels()
    private val LOCATION_REQ_CODE = 10005


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
        handleResult()
    }

    private fun handleResult(){
        getNavigationResult<String>(Constants.MAP_LOCATION)?.observe(viewLifecycleOwner, Observer {
            binding.locationTv.text = it
        })
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun init(){

        binding.locationTv.text = getString(R.string.currentLocationIsNotSet)

        binding.getLocationBtn.setOnClickListener {
            // show map
            navigateTo(HomeFragmentDirections.actionHomeFragmentToMapFragment())
        }

        binding.enableService.setOnClickListener {
            if (viewModel.isLocationServiceRunning(requireContext())){
                // stop service
                disableLocationService()
            }
            else{
                // start service
                enableLocationService()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableLocationService(){

        if (PermissionUtils.hasLocationPermission(requireContext()).not()){
            requestLocationPermission()
            return
        }
        if (LocationUtils.isGpsEnables(requireActivity()).not()){
            LocationUtils.requestToTurnOnGps(requireActivity(), LOCATION_REQ_CODE)
            return
        }
        else{

            val intent = Intent(requireContext(), ForegroundLocationService::class.java)
            intent.putExtra(Constants.LOCATION_SERVICE_NOTIFICATION_MODEL, NotificationModel(
                binding.titleEdt.text.toString(),
                binding.descriptionEdt.text.toString(),
                pendingIntent = PendingIntent.getActivity(
                    requireActivity(),
                    Random.nextInt(0, 9999)
                    , Intent(requireActivity(), MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    } ,PendingIntent.FLAG_IMMUTABLE)
            ))

            requireActivity().startService(intent)

            viewModel.setLocationServiceStatus(true)

        }

    }

    private fun disableLocationService(){
        requireActivity().stopService(Intent(requireContext(), ForegroundLocationService::class.java))
        viewModel.setLocationServiceStatus(false)
    }

    private fun requestLocationPermission() {
        PermissionUtils.requestForLocationPermission(requireActivity(),object : PermissionUtils.PermissionUtilsListener{
            override fun onGranted() {
                enableLocationService()
            }

            override fun onDenied() {
                showToast(getString(R.string.accept_locations_permission))
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == LOCATION_REQ_CODE) {
            enableLocationService()
        }
    }

    override fun onResume() {
        super.onResume()
        initObserver()
        viewModel.getLocationServiceStatus()
    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.getLocationServiceStatus().collect{
                binding.enableService.apply {
                    text = if (it) getString(R.string.stop_service) else getString(R.string.start_service)
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}