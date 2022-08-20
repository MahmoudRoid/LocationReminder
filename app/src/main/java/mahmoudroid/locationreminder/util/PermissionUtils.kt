package mahmoudroid.locationreminder.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.nabinbhandari.android.permissions.PermissionHandler
import java.util.ArrayList
import com.nabinbhandari.android.permissions.Permissions

object PermissionUtils {

    interface PermissionUtilsListener {
        fun onGranted()
        fun onDenied()
    }

    fun hasLocationPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < 23)
            return true

        return !(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
    }

    fun requestForLocationPermission(activity: Activity, listener: PermissionUtilsListener) {
        Permissions.check(activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            null,
            null,
            object : PermissionHandler() {
                override fun onGranted() {
                    listener.onGranted()
                }

                override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
                    listener.onDenied()
                }
            }
        )
    }

}