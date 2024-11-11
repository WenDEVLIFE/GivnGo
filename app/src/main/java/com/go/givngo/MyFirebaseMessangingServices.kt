package com.go.givngo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.go.givngo.R
import com.go.givngo.MyNotifications
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingServices : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Get notification data
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body

        // Show the notification
        if (title != null && body != null) {
            sendNotification(title, body)
        }
    }

    private fun sendNotification(title: String, body: String) {
        val intent = Intent(this, MyNotifications::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = "my_notifications_channel"
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.company_date) // Make sure to add an icon in your resources
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)

        // Create the NotificationChannel, required for Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Notifications", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Channel for app notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, builder.build())
    }
}
