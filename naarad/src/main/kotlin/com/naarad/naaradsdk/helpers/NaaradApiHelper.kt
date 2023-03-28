package com.naarad.naaradsdk.helpers

import android.content.Context
import android.util.Log
import com.naarad.naaradsdk.exceptions.DeviceRegistrationException
import com.naarad.naaradsdk.utils.Constants
import org.json.JSONObject


class NaaradApiHelper(context: Context, var apiKey: String) {

    private var requestHelper = Request(context)

    private fun getHeadersForNaaradApi() : MutableMap<String, String>{
        val headers = HashMap<String, String>()
        headers["Authorization"] = "Basic $apiKey"
        return headers
    }

    fun getAppData(dappName: String, callback: (response: JSONObject) -> Unit) {
        val url = Constants.NAARAD_API_BASE_URL + "dapp?name=$dappName&complete=True"
        Log.d("Naarad API", "Making get request at url: $url")
        requestHelper.makeGETRequest(url, getHeadersForNaaradApi()) {
            callback(it)
        }
    }

    fun registerDevice(jsonBody: JSONObject) {
        val url = Constants.NAARAD_API_BASE_URL + "devices"
        requestHelper.makePOSTRequest(url, getHeadersForNaaradApi(), jsonBody) { response ->
            if (response.statusCode.toString() != "200") {
                Log.i("Failed", "Registration with Naarad could not be completed.")
                throw DeviceRegistrationException("Naarad server was not able to register device. " +
                        "Check your API key.")
            }
            else {
                Log.i("Info", "Device registration with Naarad was successful.")
            }
        }
    }
}
