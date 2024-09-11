package edu.mike.frontend.taskapp.data.network

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

/**
 * Interceptor to log and optionally modify HTTP responses.
 */
class ResponseInterceptor : Interceptor {

    /**
     * Intercepts the HTTP response to log and optionally modify it.
     *
     * @param chain The interceptor chain.
     * @return The intercepted and potentially modified response.
     * @throws IOException If an I/O error occurs during the interception.
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        // Proceed with the request
        val response = chain.proceed(chain.request())

        // Convert the response body to a string
        val responseBodyString = response.body?.string()

        // Log the raw response
        println("Raw Response: $responseBodyString")

        // Optionally, modify the response here if needed before returning it to Retrofit
        // For example, you could return a fixed response body in case of error.

        // Return the response by re-creating the body with the intercepted content
        return response.newBuilder()
            .body((responseBodyString ?: "").toResponseBody(response.body?.contentType()))
            .build()
    }
}