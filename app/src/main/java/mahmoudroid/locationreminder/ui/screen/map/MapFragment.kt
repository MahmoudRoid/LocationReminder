package mahmoudroid.locationreminder.ui.screen.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import dagger.hilt.android.internal.managers.FragmentComponentManager
import mahmoudroid.locationreminder.databinding.FragmentMapBinding
import mahmoudroid.locationreminder.ui.base.BaseFragment

class MapFragment: BaseFragment(){

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = SupportMapFragment.newInstance()

        val fragmentTransaction =
            (FragmentComponentManager.findActivity(context) as AppCompatActivity).supportFragmentManager.beginTransaction()
        fragmentTransaction.add(id, mapFragment)
        fragmentTransaction.commit()

        mapFragment.getMapAsync {

        }

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}