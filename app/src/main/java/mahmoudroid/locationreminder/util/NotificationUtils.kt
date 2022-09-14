package mahmoudroid.locationreminder.util

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LifecycleService
import mahmoudroid.locationreminder.R
import mahmoudroid.locationreminder.data.NotificationModel
import mahmoudroid.locationreminder.ui.MainActivity
import java.util.*

object NotificationUtils {

    @SuppressLint("StaticFieldLeak")
    lateinit var builder: NotificationCompat.Builder
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private const val channelId = "channelId"
    private const val channelName = "channelName"


    fun showNotification(
        context: Context,
        title: String,
        message: String,
        smallIcon: Int = R.drawable.ic_launcher_foreground,
        priority: Int = NotificationCompat.PRIORITY_HIGH,
        pendingIntent: PendingIntent?
    ){

        notificationManager =  context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // checking if android version is greater than oreo(API 26) or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = NotificationCompat.Builder(context, channelId)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(priority)
                .setSmallIcon(smallIcon)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_background))
                .setContentIntent(pendingIntent)
        } else {
            builder = NotificationCompat.Builder(context)
                .setContentTitle(title).setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(priority)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_background))
                .setContentIntent(pendingIntent)
        }

        notificationManager.notify(Random().nextInt(), builder.build())

    }

    fun showNotification(
        context: Context,
        notificationModel: NotificationModel?
    ){
        notificationManager =  context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationModel?.let {
            // checking if android version is greater than oreo(API 26) or not
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(true)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = NotificationCompat.Builder(context, channelId)
                    .setContentTitle(it.title)
                    .setContentText(it.message)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(it.message))
                    .setPriority(it.priority)
                    .setSmallIcon(it.smallIcon)
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_background))
                    .setContentIntent(it.pendingIntent)
            } else {
                builder = NotificationCompat.Builder(context)
                    .setContentTitle(it.title).setContentText(it.message)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(it.message))
                    .setPriority(it.priority)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_launcher_background))
                    .setContentIntent(it.pendingIntent)
            }

            notificationManager.notify(Random().nextInt(), builder.build())
        }

    }

    fun buildNotificationForService(context: Context, notificationModel: NotificationModel): Notification{

        fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    channelId,
                    channelId,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).also {
                    it.enableVibration(true)
                }
                val manager = context.getSystemService(LifecycleService.NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(notificationChannel)
            }
        }

        // checking if android version is greater than oreo(API 26) or not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createNotificationChannel()

            return NotificationCompat.Builder(context, channelId)
                .setContentTitle(notificationModel.title)
                .setContentText(notificationModel.message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(notificationModel.message))
                .setPriority(notificationModel.priority)
                .setSmallIcon(notificationModel.smallIcon)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, notificationModel.smallIcon))
                .setContentIntent(notificationModel.pendingIntent)
                .addAction(notificationModel.smallIcon, notificationModel.stopMessage, notificationModel.stopIntent)
                .setOngoing(true)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .build()

        } else {
            return NotificationCompat.Builder(context)
                .setWhen(0)
                .setContentTitle(notificationModel.title)
                .setContentText(notificationModel.message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(notificationModel.message))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setSmallIcon(notificationModel.smallIcon)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, notificationModel.smallIcon))
                .setContentIntent(notificationModel.pendingIntent)
                .addAction(notificationModel.smallIcon, notificationModel.stopMessage, notificationModel.stopIntent)
                .setOngoing(true)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .build()
        }

    }


}