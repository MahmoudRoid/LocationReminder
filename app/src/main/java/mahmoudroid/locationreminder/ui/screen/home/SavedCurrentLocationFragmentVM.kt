package mahmoudroid.locationreminder.ui.screen.home

import android.app.ActivityManager
import android.content.Context
import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mahmoudroid.locationreminder.data.source.SharedPref
import mahmoudroid.locationreminder.service.ForegroundLocationService
import javax.inject.Inject

@HiltViewModel
class SavedCurrentLocationFragmentVM @Inject constructor(
    private val sp: SharedPref
): ViewModel(){

    var locationResult: Location? = null


    private val _locationServiceStatus = MutableStateFlow(false)
    fun getLocationServiceStatus() = _locationServiceStatus
    fun setLocationServiceStatus(status: Boolean){
        viewModelScope.launch { _locationServiceStatus.emit(status) }
    }


    fun isLocationServiceRunning(context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        for (service in manager!!.getRunningServices(Int.MAX_VALUE)) {
            if (ForegroundLocationService::class.java.name == service.service.className) {
                setLocationServiceStatus(true)
                return true
            }
        }
        setLocationServiceStatus(false)
        return false
    }

}