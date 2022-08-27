package mahmoudroid.locationreminder.workmanager

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import mahmoudroid.locationreminder.data.source.SharedPref


class LocationWorker(val context: Context, workerParams: WorkerParameters): Worker(context, workerParams) {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    var x = 0

    @SuppressLint("MissingPermission")
    override fun doWork(): Result {
        Log.i("TAG", "doWork: ")
        locationCallback = object: LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult?.let {
                    for (location in locationResult.locations){
                        location?.let {
                            x++
                            Log.i("TAG", "doWork: ${location.latitude.toString() + "//" + location.longitude.toString()}    x = $x")
                        }
                    }
                }
            }
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )



        return Result.success()

    }

}