package com.naarad.naaradsdk.devicemanagement

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.naarad.naaradsdk.exceptions.DeviceRegistrationException
import com.naarad.naaradsdk.helpers.NaaradApiHelper
import com.naarad.naaradsdk.helpers.Request
import org.json.JSONObject
import com.naarad.naaradsdk.tokenmanagement.TokenHelper


class DeviceRegisterer(context: Context, var apiKey: String) {

    private var naaradApiHelper = NaaradApiHelper(context, apiKey)

    fun registerDevice(walletAddress: String, dappName: String) {
        TokenHelper().getDeviceToken { deviceToken ->
            if (deviceToken == null) {
                Log.d(TAG, "Failed to register device to Naarad. " +
                        "Device token could not be retrieved.")
                throw DeviceRegistrationException("Could not retrieve device token.")
            }
            else {
                registerDevice(walletAddress, deviceToken, dappName)
            }
        }
    }

    private fun createJsonObjectForRegistration(walletAddress: String, deviceToken: String,
                                                dappName: String) : JSONObject{
        val jsonBody = JSONObject()
        jsonBody.put("api_key", apiKey)
        jsonBody.put("wallet_address", walletAddress)
        jsonBody.put("device_token", deviceToken)
        jsonBody.put("dapp_name", dappName)
        return jsonBody
    }

    private fun registerDevice(walletAddress: String, deviceToken: String, dappName: String) {
        // Make call to api backend to register device using wallet address,
        // device token and dapp name
        val jsonBody = createJsonObjectForRegistration(walletAddress, deviceToken, dappName)
        naaradApiHelper.registerDevice(jsonBody)
    }
}