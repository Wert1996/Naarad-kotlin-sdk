package com.naarad.naaradsdk.services

import android.content.ContentValues
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.naarad.naaradsdk.models.NaaradRemoteMessage


class NaaradMessagingService : FirebaseMessagingService() {
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

    fun onPushNotificationReceived(remoteMessage: NaaradRemoteMessage) {
        Log.d("Naarad notification", "Process notification by overriding this method")
    }
}