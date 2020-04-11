package za.co.gingergeek.moneta.net

import android.content.Context
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class RequestManager(context: Context) {
    private val socketTimeout = 5000
    private val restTag = "REST"
    private val queue: RequestQueue = Volley.newRequestQueue(context)

    fun clearQueue() {
        queue.cancelAll(restTag)
    }

    fun makeRequest(
        url: String,
        responseListener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ) {
        val request = StringRequest(Request.Method.GET, url, responseListener, errorListener)

        request.tag = restTag
        request.retryPolicy = DefaultRetryPolicy(
            socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queue.add(request)
    }
}