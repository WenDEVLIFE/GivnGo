package com.go.givngo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.PowerManager

import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.go.givngo.R
import com.go.givngo.MyNotifications
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import android.util.Log
import com.go.givngo.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

import androidx.navigation.compose.composable


class MyFirebaseMessagingService : FirebaseMessagingService() {

private lateinit var wakeLock: PowerManager.WakeLock

    
    private fun sendNotification(title: String, body: String) {
        val intent = Intent(this, MyNotifications::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = "my_notifications_channel"
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.company_date) // Ensure this icon is in your resources
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(this)

        // Create notification channel for Android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Notifications", NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Channel for app notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, builder.build())
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
    val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
    wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyApp::WakeLock")
    wakeLock.acquire(10*60*1000L /*10 minutes*/) // Keep CPU awake for 10 minutes

    // Process your message here

    val title = remoteMessage.data["title"] ?: remoteMessage.notification?.title
    val body = remoteMessage.data["body"] ?: remoteMessage.notification?.body
    val email = remoteMessage.data["email"] ?: return

    if (title != null && body != null) {
        sendNotification(title, body)
    }
    
        // Release the wake lock after task is done
    wakeLock.release()
}

}
