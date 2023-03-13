package com.naarad.naaradsdk.models

import android.util.Log
import com.google.firebase.messaging.RemoteMessage

class NaaradRemoteMessage() {
    private var messageData: MutableMap<String, String>? = null;
    private var notificationBody: String? = "";

    constructor(remoteMessage: RemoteMessage) : this() {
        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("Naarad Message", "Message data payload: ${remoteMessage.data}")
            messageData = remoteMessage.data
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("Naarad Message", "Message Notification Body: ${it.body}")
            notificationBody = it.body
        }
    }
}