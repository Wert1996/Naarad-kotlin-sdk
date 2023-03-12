package com.naarad.naaradsdk.tokenmanagement

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class TokenHelper {
    fun getDeviceToken(callback: (String?) -> Unit) {
        Firebase.messaging.getToken().addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                callback(null)
            }

            // Get new FCM registration token
            val token = task.result

            callback(token.toString())
        }
    }
}