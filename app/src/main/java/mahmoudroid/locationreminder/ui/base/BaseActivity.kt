package mahmoudroid.locationreminder.ui.base

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import mahmoudroid.locationreminder.R
import mahmoudroid.locationreminder.ui.MainActivity
import mahmoudroid.locationreminder.util.NotificationUtils
import kotlin.random.Random

open class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    fun changeNotificationBarIconsColor(lightMode: Boolean = false) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) window.decorView.systemUiVisibility =  if (lightMode) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else View.SYSTEM_UI_FLAG_LAYOUT_STABLE
    }

    @SuppressLint("ObsoleteSdkInt")
    fun changeStatusBarColor(colorId: Int = R.color.backgroundColor) {
        if (Build.VERSION.SDK_INT > 20) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, colorId)
        }
    }

    // notification
    fun showRegularNotification(
        title: String = getString(R.string.title),
        message: String = getString(R.string.message)
    ){
        NotificationUtils.showNotification(
            context = this,
            title = title,
            message = message,
            pendingIntent = PendingIntent.getActivity(
                this,
                Random.nextInt(0, 9999)
                , Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                } ,0)
        )
    }

}