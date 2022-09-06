package mahmoudroid.locationreminder.ui

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import mahmoudroid.locationreminder.databinding.ActivityMainBinding
import mahmoudroid.locationreminder.ui.base.BaseActivity
import mahmoudroid.locationreminder.util.NotificationUtils

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


        showRegularNotification("title","message for test")

    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}