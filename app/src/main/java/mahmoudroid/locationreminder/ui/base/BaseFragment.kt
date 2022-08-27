package mahmoudroid.locationreminder.ui.base

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import mahmoudroid.locationreminder.R
import mahmoudroid.locationreminder.ext.hideKeyboard
import mahmoudroid.locationreminder.ext.navigateSafe
import mahmoudroid.locationreminder.ui.MainActivity
import mahmoudroid.locationreminder.util.NotificationUtils
import kotlin.random.Random.Default.nextInt

open class BaseFragment: Fragment() {

    override fun onPause() {
        this.hideKeyboard()
        super.onPause()
    }

    fun navigateTo(navDirection: NavDirections) {
        try {
            findNavController().navigateSafe(navDirection)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onBackPressed() {

        try {
            findNavController().navigateUp()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun changeStatusBarColor(colorId: Int = R.color.colorPrimary) {
        if (Build.VERSION.SDK_INT > 20) {
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireContext(), colorId)
        }
    }

    fun changeNotificationBarIconsColor(lightMode: Boolean = false) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) requireActivity().window.decorView.systemUiVisibility =
            if (lightMode) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    fun closeApplication() = requireActivity().finish()


    // show message
    fun showToast(message: String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }


    // notification
    fun showRegularNotification(
        title: String = getString(R.string.title),
        message: String = getString(R.string.message)
    ){
        NotificationUtils.showRegularNotification(
            context = requireContext(),
            title = title,
            message = message,
            pendingIntent = PendingIntent.getActivity(
                requireActivity(),
                nextInt(0,9999)
                , Intent(requireActivity(), MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                } ,0)
        )
    }
}