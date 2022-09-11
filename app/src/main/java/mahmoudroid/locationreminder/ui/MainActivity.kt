package mahmoudroid.locationreminder.ui

import android.os.Bundle
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import mahmoudroid.locationreminder.data.source.SharedPref
import mahmoudroid.locationreminder.databinding.ActivityMainBinding
import mahmoudroid.locationreminder.ui.base.BaseActivity
import mahmoudroid.locationreminder.util.NotificationUtils
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    @Inject lateinit var sharedPref: SharedPref

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