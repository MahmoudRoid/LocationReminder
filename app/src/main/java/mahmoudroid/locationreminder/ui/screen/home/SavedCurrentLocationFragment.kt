package mahmoudroid.locationreminder.ui.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mahmoudroid.locationreminder.databinding.FragmentSavedCurrentLocationBinding
import mahmoudroid.locationreminder.ui.base.BaseFragment

class SavedCurrentLocationFragment: BaseFragment() {

    private var _binding: FragmentSavedCurrentLocationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedCurrentLocationBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}