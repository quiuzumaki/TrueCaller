package com.example.truecaller

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.truecaller.defaultcall.ContactDetail
import com.example.truecaller.defaultcall.INFO_STATUS
import com.example.truecaller.screencallapp.DialerActivity

class CallerNotification(private val context: Context){
    private val NOTIFICATION_CHANNEL_NAME = "notification_true_caller"
    companion object {
        const val INCOMING_NOTIFICATION_ID: String = "caller_notification_incoming"
        const val OUTGOING_NOTIFICATION_ID: String = "caller_notification_outgoing"

        const val INCOMING_CHANNEL_ID: Int = 1
        const val OUTGOING_CHANNEL_ID: Int = 2

        @RequiresApi(Build.VERSION_CODES.O)
        fun cancelNotification(context: Context, id: String) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.deleteNotificationChannel(id)
        }

        @RequiresApi(Build.VERSION_CODES.P)
        fun showNotification(context: Context, contactDetail: ContactDetail) {
            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val intent = Intent(context, DialerActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                action = Intent.ACTION_ANSWER
            }

            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            val largeIconId = when(contactDetail.infoStatus) {
                INFO_STATUS.SPAM -> R.drawable.ic_spam
                INFO_STATUS.UNKNOWN -> R.drawable.ic_warn
                else -> {R.drawable.ic_phone_paused_24}
            }
            val notification: Notification = NotificationCompat.Builder(context, INCOMING_NOTIFICATION_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, largeIconId))
                .setContentTitle(contactDetail.phoneNumber)
                .setContentText(contactDetail.infoStatus.toString())
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(NotificationCompat.BigTextStyle().bigText(contactDetail.infoStatus.toString()))
                .setContentIntent(pendingIntent).addAction(androidx.core.R.drawable.ic_call_answer, "Add Spam number", pendingIntent)
                .build()

            // create notification channel
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (notificationManager.getNotificationChannel(INCOMING_NOTIFICATION_ID) == null) {
                    val channel = NotificationChannel(
                        INCOMING_NOTIFICATION_ID,
                        "notification_true_caller",
                        IMPORTANCE_HIGH
                    )
                    channel.description = "true_caller"
                    notificationManager.createNotificationChannel(channel)
                }
            }
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notificationManager.notify(INCOMING_CHANNEL_ID, notification)
        }
    }
}