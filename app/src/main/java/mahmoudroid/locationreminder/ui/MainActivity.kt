package mahmoudroid.locationreminder.ui

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import mahmoudroid.locationreminder.databinding.ActivityMainBinding
import mahmoudroid.locationreminder.ui.base.BaseActivity

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        changeNotificationBarIconsColor()
        changeStatusBarColor()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}