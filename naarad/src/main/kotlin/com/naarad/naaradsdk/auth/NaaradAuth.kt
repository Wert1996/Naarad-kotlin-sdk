package com.naarad.naaradsdk.auth

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.naarad.naaradsdk.devicemanagement.DeviceRegisterer
import com.naarad.naaradsdk.exceptions.InitialisationException
import com.naarad.naaradsdk.helpers.NaaradApiHelper


class NaaradAuth(var context: Context, var dappName: String,
                 var apiKey: String, var walletAddress: String) {
    private var deviceRegisterer = DeviceRegisterer(context, apiKey)

    fun initialiseApp() {
        Log.i("Naarad SDK:", "Initialising Naarad...")
        NaaradApiHelper(context, apiKey).getAppData(dappName) { appData ->
            if (appData.length() == 0) {
                throw InitialisationException("A dapp with provided name does not exist. " +
                        "Make sure your dApp is registered.")
            }
            if (!appData.has("firebase_data")) {
                throw InitialisationException("Incomplete data received from Naarad API.")
            }
            val applicationId = appData.getJSONObject("firebase_data")
                .getString("app_id")
                ?: throw InitialisationException("Application ID not returned by Naarad API.")
            val firebaseApiKey = appData.getJSONObject("firebase_data")
                .getString("api_key")
                ?: throw InitialisationException("Firebase API Key not returned by Naarad API.")
            val projectId = appData.getJSONObject("firebase_data")
                .getString("project_id")
                ?: throw InitialisationException("Project ID not returned by Naarad API.")
            initialiseFirebase(applicationId, projectId, firebaseApiKey)
            deviceRegisterer.registerDevice(walletAddress, dappName)
            Log.i("Naarad SDK:", "Successfully initialised.")
        }
    }

    private fun initialiseFirebase(appId: String, projectId: String, firebaseApiKey: String) {
        val firebaseOptions = FirebaseOptions.Builder()
            .setApplicationId(appId)
            .setApiKey(firebaseApiKey)
            .setProjectId(projectId)
            .build();
        try {
            FirebaseApp.initializeApp(context, firebaseOptions)
        }
        catch (e: Exception) {
            throw InitialisationException("Encountered error while initialising Firebase. " +
                    "Error: ${e.message}")
        }
    }
}
