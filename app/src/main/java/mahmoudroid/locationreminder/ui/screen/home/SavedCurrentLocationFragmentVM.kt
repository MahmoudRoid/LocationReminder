package mahmoudroid.locationreminder.ui.screen.home

import android.location.Location
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import mahmoudroid.locationreminder.data.source.SharedPref
import javax.inject.Inject

@HiltViewModel
class SavedCurrentLocationFragmentVM @Inject constructor(
    private val sp: SharedPref
): ViewModel(){

    var currentLocation: Location? = null

}