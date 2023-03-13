package com.naarad.naaradsdk.helpers


import android.content.Context
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException


class Request(var context: Context) {
    private var requestQueue: RequestQueue = Volley.newRequestQueue(context)

    fun makePOSTRequest(url: String, headers: MutableMap<String, String>, jsonBody: JSONObject,
                        callback: (response: NetworkResponse) -> Unit) {

        try {
            val requestBody = jsonBody.toString()
            val stringRequest: StringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response -> Log.i("Successful request.", response!!) },
                Response.ErrorListener { error ->
                        Log.e("Error on making POST request", error.toString())
                        throw error
            }) {
                override fun getBodyContentType(): String {
                    return "application/json; charset=utf-8"
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray? {
                    return try {
                        requestBody.toByteArray(charset("utf-8"))
                    } catch (uee: UnsupportedEncodingException) {
                        VolleyLog.wtf(
                            "Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody,
                            "utf-8"
                        )
                        null
                    }
                }

                override fun getHeaders(): MutableMap<String, String> {
                    return headers
                }

                override fun parseNetworkResponse(response: NetworkResponse): Response<String> {
                    var responseString = response.statusCode.toString()
                    Log.i("Response data", response.data.toString())
                    callback(response)
                    return Response.success(
                        responseString,
                        HttpHeaderParser.parseCacheHeaders(response)
                    )
                }
            }
            requestQueue.add(stringRequest)
        } catch (error: JSONException) {
//            e.printStackTrace()
            throw error
        }

    }

    fun makeGETRequest(url: String, headers: MutableMap<String, String>,
                       callback: (response: JSONObject) -> Unit) {

        val requestQueue: RequestQueue = Volley.newRequestQueue(context)

        val stringRequest = object : StringRequest(
            Method.GET, url,
            Response.Listener { response ->
                Log.i("Response", response)
                callback(JSONObject(response))
            }, Response.ErrorListener { error ->
                Log.i("Error", "[$error]")
                throw error
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                return headers
            }
        }

        requestQueue.add(stringRequest)

    }
}