package mahmoudroid.locationreminder.ui.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mahmoudroid.locationreminder.databinding.FragmentSavedDestinationLocationBinding
import mahmoudroid.locationreminder.ui.base.BaseFragment

class SavedDestinationLocationFragment: BaseFragment() {

    private var _binding: FragmentSavedDestinationLocationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedDestinationLocationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}