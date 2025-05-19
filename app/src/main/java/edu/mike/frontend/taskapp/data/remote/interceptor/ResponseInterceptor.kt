package edu.mike.frontend.taskapp.data.remote.interceptor

    import android.util.Log
    import okhttp3.Interceptor
    import okhttp3.Response
    import okhttp3.ResponseBody.Companion.toResponseBody
    import java.io.IOException
    import javax.inject.Inject

    /**
     * Interceptor to log and optionally modify HTTP responses.
     */
    class ResponseInterceptor @Inject constructor() : Interceptor {

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
            val originalResponse = chain.proceed(chain.request())

            // Get the response body
            val responseBody = originalResponse.body

            if (responseBody != null) {
                // Read the response body content
                val responseBodyString = responseBody.string()

                // Log the raw response
                Log.d("ResponseInterceptor", "Raw Response: $responseBodyString")

                // Create a new response with a new response body
                return originalResponse.newBuilder()
                    .body(responseBodyString.toResponseBody(responseBody.contentType()))
                    .build()
            }

            // If response body is null, just return the original response
            return originalResponse
        }
    }