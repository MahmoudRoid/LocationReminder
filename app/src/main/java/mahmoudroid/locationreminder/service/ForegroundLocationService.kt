package mahmoudroid.locationreminder.service

import android.app.PendingIntent
import android.content.Intent
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mahmoudroid.locationreminder.R
import mahmoudroid.locationreminder.data.Constants
import mahmoudroid.locationreminder.data.Constants.ACTION_STOP_UPDATES
import mahmoudroid.locationreminder.data.LocationRepository
import mahmoudroid.locationreminder.data.NotificationModel
import mahmoudroid.locationreminder.data.source.SharedPref
import mahmoudroid.locationreminder.ui.MainActivity
import mahmoudroid.locationreminder.util.LocationUtils
import mahmoudroid.locationreminder.util.NotificationUtils
import mahmoudroid.locationreminder.util.PermissionUtils
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class ForegroundLocationService: LifecycleService() {

    @Inject
    lateinit var locationRepository: LocationRepository
    @Inject lateinit var sharedPref: SharedPref
    private var started = false
    private var notificationModel: NotificationModel? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i("TAG", "onStartCommand: ")
        super.onStartCommand(intent, flags, startId)

        // This action comes from our ongoing notification. The user requested to stop updates.
        if (intent?.action == ACTION_STOP_UPDATES){
            stopForegroundService()
            return STOP_FOREGROUND_REMOVE
        }

        intent?.let {
            notificationModel = it.extras?.getParcelable(Constants.LOCATION_SERVICE_NOTIFICATION_MODEL)
            notificationModel?.stopIntent = PendingIntent.getService(
                this@ForegroundLocationService,
                0,
                Intent(this@ForegroundLocationService, this@ForegroundLocationService::class.java).setAction(ACTION_STOP_UPDATES),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        if (started.not()){
            started = true
            lifecycleScope.launch(Dispatchers.Default) {
                if (LocationUtils.isGpsEnables(this@ForegroundLocationService)){
                    if (PermissionUtils.hasLocationPermission(this@ForegroundLocationService)){
                        locationRepository.startLocationUpdates()
                    }
                }
            }
        }

        lifecycleScope.launch(Dispatchers.Default) {
            locationRepository.lastLocation.collect(::receiveLocation)
        }

        startForeground(1, NotificationUtils.buildNotificationForService(this, NotificationModel(
            title = getString(R.string.location_service),
            message = getString(R.string.service_is_running),
            stopMessage = getString(R.string.stop_service),
            pendingIntent = notificationModel!!.pendingIntent,
            stopIntent = notificationModel!!.stopIntent
            )
        ))

        return START_STICKY
    }

    private fun stopForegroundService(){

        locationRepository.stopLocationUpdates()
        stopForeground(true)
        stopSelf()

    }

    private val sampleLocation: Location = Location("").apply {
        latitude = 35.7609067
        longitude = 51.4036601
    }

    private fun receiveLocation(location: Location?){
        location?.let {

            lifecycleScope.launch(Dispatchers.Main){
                Toast.makeText(this@ForegroundLocationService, LocationUtils.distanceInMeter(sampleLocation, it).toString(), Toast.LENGTH_SHORT).show()
            }

            if (LocationUtils.hasUserLeftCurrentLocation(sampleLocation, it) ){
                stopForegroundService()
                NotificationUtils.showNotification(
                    this,
                    notificationModel
                )
            }
        }
    }

}