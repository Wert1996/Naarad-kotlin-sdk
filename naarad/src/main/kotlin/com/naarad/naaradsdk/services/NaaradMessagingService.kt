package com.naarad.naaradsdk.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.naarad.naaradsdk.R
import com.naarad.naaradsdk.models.NaaradRemoteMessage
import org.json.JSONObject


open class NaaradMessagingService : FirebaseMessagingService() {
    val DEFAULT_CHANNEL_ID = "naaradChannelId"
    val DEFAULT_CHANNEL_NAME = "naaradChannelName"
    var DEFAULT_CHANNEL_DESCRIPTION = "naaradChannelDescription"
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Send token to app server on miscellaneous calls to onNewToken
        Log.d(ContentValues.TAG, "sendRegistrationTokenToServer($token)")
    }

    override fun onNewToken(token: String) {
        Log.d(ContentValues.TAG, "Refreshed firebase token: $token")

        sendRegistrationToServer(token)
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("Naarad Message Listener", "From: ${remoteMessage.from}")

        val naaradRemoteMessage = NaaradRemoteMessage(remoteMessage)
        onPushNotificationReceived(naaradRemoteMessage)
    }

    open fun onPushNotificationReceived(remoteMessage: NaaradRemoteMessage) {
        Log.d("Naarad notification", "Process notification by overriding this method")
        if (checkValidMessage(remoteMessage)) {
            showNotification(buildNotification(remoteMessage))
        }
        else {
            Log.i("Notification error", "Invalid notification body. No data present. " +
                    "Aborting notification.")
        }
    }

    private fun checkValidMessage(remoteMessage: NaaradRemoteMessage) : Boolean {
        val data = remoteMessage.messageData?.get("data")
        if(data == null) {
            Log.i("Notification error", "Invalid notification body. No data present. " +
                    "Aborting notification.")
            return false
        }
        else {
            val dataObj = JSONObject(data)
            val type = dataObj.getString("type")
            val description = dataObj.getString("description")
            return if (type == null || description == null) {
                Log.i("Notification error", "Invalid notification body. No data present. " +
                        "Aborting notification.")
                false
            } else {
                true
            }
        }
    }

    open fun buildNotification(remoteMessage: NaaradRemoteMessage): NotificationCompat.Builder? {
        if(NotificationManagerCompat.from(this)
                .getNotificationChannel(DEFAULT_CHANNEL_ID) == null) {
            createNotificationChannel()
        }
        val data = remoteMessage.messageData?.get("data")?.let { JSONObject(it) }
        val type = data?.getString("type")
        val description = data?.getString("description")
        return NotificationCompat.Builder(this, DEFAULT_CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(type)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    private fun getNotificationId() : Int {
        return (0..10000000).random()
    }

    open fun showNotification(builder: NotificationCompat.Builder?) {
        val context = this
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                return
            }
            if (builder != null) {
                notify(getNotificationId(), builder.build())
            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(DEFAULT_CHANNEL_ID,
                DEFAULT_CHANNEL_NAME, importance).apply {
                description = DEFAULT_CHANNEL_DESCRIPTION
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}