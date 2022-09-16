package mahmoudroid.locationreminder.util

import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import mahmoudroid.locationreminder.data.Constants.DISTANCE_THRESHOLD
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object LocationUtils {


    fun isGpsEnables(context: Context): Boolean {
        return try {
            val locationManager =
                context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun requestToTurnOnGps(activity: Activity, requestCode: Int = 10021) {
        val googleApiClient = GoogleApiClient.Builder(activity).addApi(LocationServices.API).build()
        googleApiClient.connect()

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 30 * 1000.toLong()
        locationRequest.fastestInterval = 5 * 1000.toLong()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        builder.setAlwaysShow(true)

        LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
            .setResultCallback { result ->
                val status: Status = result.status
                when (status.statusCode) {
                    LocationSettingsStatusCodes.SUCCESS -> {}
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        status.startResolutionForResult(activity, requestCode)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                }
            }
    }

    fun distanceInMeter(savedLocation: Location, currentLocation: Location): Double{

        fun deg2rad(deg: Double) = (deg * Math.PI / 180.0)

        val dLat = deg2rad(currentLocation.latitude-savedLocation.latitude)
        val dLon = deg2rad(currentLocation.longitude-savedLocation.longitude)
        val a = sin(dLat/2) * sin(dLat/2) +
                cos(deg2rad(savedLocation.latitude)) * cos(deg2rad(currentLocation.latitude)) *
                sin(dLon/2) * sin(dLon/2)
        val c = 2 * atan2(sqrt(a), sqrt(1-a))
        val d = 6371 * c // Distance in km

        Log.i("TAG", "distanceInMeter: ${(d * 1000).toString().take(5)}")
        return d * 1000
    }

    fun hasUserLeftCurrentLocation(savedLocation: Location, currentLocation: Location) = distanceInMeter(savedLocation,currentLocation) > DISTANCE_THRESHOLD

    fun hasUserArrivedToDestination(destinationLocation: Location, currentLocation: Location) = distanceInMeter(destinationLocation,currentLocation) < DISTANCE_THRESHOLD

}