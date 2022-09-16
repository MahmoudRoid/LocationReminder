package mahmoudroid.locationreminder.ui.base

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.internal.managers.FragmentComponentManager
import mahmoudroid.locationreminder.R
import mahmoudroid.locationreminder.util.PermissionUtils

class BaseMapView: FrameLayout, GoogleMap.OnMarkerClickListener {

    private lateinit var googleMap: GoogleMap
    private var iMapViewCallBacks: IMapViewCallBacks? = null
    private val markers = ArrayList<Marker>()
    private lateinit var marker: Marker

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
        init(context)
    }

    constructor(context: Context,
                attrs: AttributeSet?,
                defStyleAttr: Int,
                defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    @SuppressLint("MissingPermission")
    private fun init(context: Context) {

        val mapFragment = SupportMapFragment.newInstance()

        if (!isInEditMode) {

//            val fragmentTransaction = requireParentFragment().childFragmentManager.beginTransaction()
            val fragmentTransaction = (FragmentComponentManager.findActivity(context) as AppCompatActivity).supportFragmentManager.beginTransaction()
            fragmentTransaction.add(id, mapFragment)
            fragmentTransaction.commit()

            mapFragment.getMapAsync {
                googleMap = it
                if (::googleMap.isInitialized) {

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(35.7606723, 51.4042605), 17f))

                    googleMap.uiSettings.isZoomControlsEnabled = false
                    googleMap.uiSettings.setAllGesturesEnabled(true)
                    googleMap.setOnMarkerClickListener(this@BaseMapView)
                    googleMap.uiSettings.isScrollGesturesEnabled = true
                    googleMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = true
                    googleMap.uiSettings.isTiltGesturesEnabled = true
                    googleMap.uiSettings.isMapToolbarEnabled = true
                    googleMap.uiSettings.isMyLocationButtonEnabled = false
                    iMapViewCallBacks?.mapIsReady()
                    googleMap.setOnMapLoadedCallback {}
                    googleMap.setOnCameraIdleListener { iMapViewCallBacks?.onCameraMove() }
                }
            }

        }

    }

    fun disableAllMapGestures() {
        if (::googleMap.isInitialized) {
            googleMap.uiSettings.setAllGesturesEnabled(false)
            googleMap.uiSettings.isScrollGesturesEnabled = false
            googleMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = false
            googleMap.uiSettings.isTiltGesturesEnabled = false
            googleMap.uiSettings.isMapToolbarEnabled = false
        }
    }

    fun setOnMapReadyListener(imapViewCallBacks: IMapViewCallBacks) {
        this.iMapViewCallBacks = imapViewCallBacks
    }

    fun moveToCurrentLocation(lat: Double, lng: Double, zoom: Float = 16f) {
        if (::googleMap.isInitialized) {
            val location = CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), zoom)
            googleMap.animateCamera(location)
        }
    }

    fun getMapCenter(): LatLng {
        return googleMap.cameraPosition.target
    }

    fun setMapMarkerOption(latLng: LatLng) {
        if (::googleMap.isInitialized) googleMap.clear()
        val marker = MarkerOptions().position(latLng)

        if (::googleMap.isInitialized) {
            googleMap.addMarker(marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_map)))
        }
    }

    fun addMarker(latLngArrayList: ArrayList<LatLng>) {
        if (!markers.isEmpty()) {
            for (oldMarker in markers) {
                oldMarker.remove()
            }
        }
        var markerOptions: MarkerOptions
        var position = 0
        for (latLng in latLngArrayList) {
            markerOptions = MarkerOptions().position(latLng!!)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_map))
            val marker: Marker = googleMap.addMarker(markerOptions)!!
            marker.tag = position
            markers.add(marker)
            position += 1
        }
    }

    @SuppressLint("MissingPermission")
    fun setMyLocationEnable() {
        if (::googleMap.isInitialized) {
            if (PermissionUtils.hasLocationPermission(context)) {
                googleMap.isMyLocationEnabled = true
            }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        iMapViewCallBacks?.onMarkerClick(marker)
        return true
    }

    interface IMapViewCallBacks {
        fun mapIsReady()
        fun onCameraMove()
        fun onMarkerClick(marker: Marker)
    }

}