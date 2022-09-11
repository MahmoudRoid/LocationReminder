package mahmoudroid.locationreminder.data

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class LocationRepository @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient
) {
    private val callback = Callback()

    private val _isReceivingUpdates = MutableStateFlow(false)
    val isReceivingLocationUpdates = _isReceivingUpdates.asStateFlow()

    private val _lastLocation = MutableStateFlow<Location?>(null)
    val lastLocation = _lastLocation.asStateFlow()

    @SuppressLint("MissingPermission") // Only called when holding location permission.
    fun startLocationUpdates() {
        val request = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10_000 // 10 seconds
        }
        fusedLocationProviderClient.requestLocationUpdates(
            request,
            callback,
            Looper.getMainLooper()
        )
        _isReceivingUpdates.value = true
    }

    fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(callback)
        _isReceivingUpdates.value = false
        _lastLocation.value = null
    }

    private inner class Callback : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            _lastLocation.value = result.lastLocation
        }
    }
}
