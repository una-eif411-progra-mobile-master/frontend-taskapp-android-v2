package edu.mike.frontend.taskapp.data.remote.interceptor

import edu.mike.frontend.taskapp.data.local.AuthPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Interceptor that adds authentication token to API requests.
 *
 * This interceptor automatically adds the authentication token to the
 * Authorization header for requests that require authentication.
 */
@Singleton
class AuthInterceptor @Inject constructor(
    private val authPreferences: AuthPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Skip authentication for login and registration endpoints
        if (originalRequest.url.encodedPath.contains("auth/login") ||
            originalRequest.url.encodedPath.contains("auth/register")
        ) {
            return chain.proceed(originalRequest)
        }

        // Get the token from AuthPreferences
        val token = runBlocking {
            authPreferences.getAuthToken()
        }

        // If no token is available, proceed with the original request
        if (token.isNullOrBlank()) {
            return chain.proceed(originalRequest)
        }

        // Add the token to the request header
        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}