package mahmoudroid.locationreminder.data

import android.app.PendingIntent
import android.os.Parcelable
import androidx.core.app.NotificationCompat
import kotlinx.android.parcel.Parcelize
import mahmoudroid.locationreminder.R

@Parcelize
data class NotificationModel(
    val title: String,
    val message: String,
    val smallIcon: Int = R.mipmap.ic_launcher,
    val priority: Int = NotificationCompat.PRIORITY_HIGH,
    val stopMessage: String = "",
    val pendingIntent: PendingIntent,
    var stopIntent: PendingIntent? = null,
): Parcelable
