package com.naarad.naaradsdk.services

import android.content.ContentValues
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class NaaradMessagingService : FirebaseMessagingService() {
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(ContentValues.TAG, "sendRegistrationTokenToServer($token)")
    }

    override fun onNewToken(token: String) {
        Log.d(ContentValues.TAG, "Refreshed firebase token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token)
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("Message Listener", "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("Message listener", "Message data payload: ${remoteMessage.data}")
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("Message listener", "Message Notification Body: ${it.body}")
        }
    }
}