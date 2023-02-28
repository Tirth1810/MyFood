package com.example.myapp.Activity

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.myapp.Fragments.CartFragment
import com.example.myapp.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.security.Provider.Service

const val channelId = "notification_channel"
const val channelName = "com.example.myapp.Activity"

class MyFirebaseNotificationService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        pushNotification(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!)
    }

    private fun pushNotification(title: String, msg: String) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(this, CartFragment::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_IMMUTABLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "Book will be delivered in 12 days"
            val description = "My Food"
            val imp = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, imp)
            val notification: Notification
            channel.description = description
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel)
                notification = Notification.Builder(this).setSmallIcon(R.drawable.burgers)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(name)
                    .setSubText(description)
                    .setAutoCancel(true)
                    .setChannelId(channelId)
                    .build()

            } else {
                notification = Notification.Builder(this).setSmallIcon(R.drawable.burgers)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(name)
                    .setSubText(description)
                    .setAutoCancel(true)
                    .setChannelId(channelId)
                    .build()
            }
            if (notificationManager != null) {
                notificationManager.notify(1, notification)
            }
        }
    }


}