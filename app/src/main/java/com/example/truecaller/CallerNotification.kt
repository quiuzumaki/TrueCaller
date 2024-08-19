package com.example.truecaller

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.Person
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat.startForeground


class CallerNotification(private val context: Context){

    companion object {
        val INCOMING_NOTIFICATION_ID: String = "caller_notification_incoming"
        val OUTGOING_NOTIFICATION_ID: String = "caller_notification_outgoing"

        val INCOMING_CHANNEL_ID: Int = 1
        val OUTGOING_CHANNEL_ID: Int = 2

        @RequiresApi(Build.VERSION_CODES.O)
        fun getNotificationID(context: Context) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channels = notificationManager.notificationChannels
            channels.forEach { channel ->
                Log.d("Channel: name", channel.name.toString())
                Log.d("Channel: importance", channel.importance.toString())
                Log.d("Channel: id", channel.id)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun cancelNotification(context: Context, id: String) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.deleteNotificationChannel(id)
        }

    }

    private val ringToneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
    private lateinit var notificationManager: NotificationManager

    init {
        this.notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelNameRes: Int, importance: Int) {
        val channel = NotificationChannel(channelId, channelNameRes.toString(), importance)
        channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(channel)
    }



    @RequiresApi(Build.VERSION_CODES.P)
    public fun showIncomingNotification() {

    }

    public fun showOutgoingNotification() {
        val ongoingChannel = NotificationChannelCompat.Builder(
            OUTGOING_NOTIFICATION_ID,
            NotificationManagerCompat.IMPORTANCE_DEFAULT,
        )
            .setName("Ongoing calls")
            .setDescription("Displays the ongoing call notifications")
            .build()
    }
}