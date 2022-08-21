package mahmoudroid.locationreminder.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import mahmoudroid.locationreminder.R
import java.util.*

object NotificationUtils {

    fun showRegularNotification(context: Context,
                         title: String,
                         message: String,
                         smallIcon: Int = R.drawable.ic_launcher_foreground,
                         channel: String = "Normal",
                         priority: Int = NotificationCompat.PRIORITY_HIGH,
                         pendingIntent: PendingIntent?) {
        val builder = NotificationCompat.Builder(context, channel).setSmallIcon(smallIcon)
            .setContentTitle(title).setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(priority).setContentIntent(pendingIntent).build()

        showNotification(context, builder, channel)
    }

    private fun showNotification(context: Context,
                                 notification: Notification,
                                 channel: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannel(context, channel)
        notification.flags = Notification.FLAG_AUTO_CANCEL
        getNotificationManager(context).notify(Random().nextInt(), notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context,
                                          channel: String,
                                          importance: Int = NotificationManager.IMPORTANCE_HIGH) {
        val notificationChannel = NotificationChannel(channel, channel, importance)
        getNotificationManager(context).createNotificationChannel(notificationChannel)

    }

    private fun getNotificationManager(context: Context) =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

}