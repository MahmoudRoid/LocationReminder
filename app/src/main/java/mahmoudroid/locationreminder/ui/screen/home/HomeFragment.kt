package mahmoudroid.locationreminder.ui.screen.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_home.view.*
import mahmoudroid.locationreminder.R
import mahmoudroid.locationreminder.databinding.FragmentHomeBinding
import mahmoudroid.locationreminder.ui.base.BaseFragment

class HomeFragment: BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeStatusBarColor(R.color.colorPrimary)
        changeNotificationBarIconsColor(true)
        initPages()
    }

    private fun initPages() {
        try {

            val currentLocationFragment = SavedCurrentLocationFragment()
            val destinationLocationFragment = SavedDestinationLocationFragment()

            val adapter: FragmentStateAdapter =
                object : FragmentStateAdapter(childFragmentManager, lifecycle) {
                    override fun createFragment(position: Int): Fragment {
                        return if (position == 0) currentLocationFragment else destinationLocationFragment
                    }

                    override fun getItemCount(): Int {
                        return 2
                    }
                }
            binding.viewPager.adapter = adapter
            binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    //updateCurrentTab(position == 0)
                }
            })

            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = if (position == 0) getString(R.string.currentLocation) else getString(R.string.destinationLocation)
            }.attach()


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

}