package edu.mike.frontend.taskapp.data.remote

import android.util.Log
import edu.mike.frontend.taskapp.data.mapper.AuthMapper
import edu.mike.frontend.taskapp.data.remote.api.AuthService
import edu.mike.frontend.taskapp.data.remote.dto.AuthRequestDto
import edu.mike.frontend.taskapp.domain.model.AuthResult
import edu.mike.frontend.taskapp.domain.model.Credentials
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

/**
 * Remote data source for authentication operations.
 * Handles all network operations related to authentication using [AuthService].
 */
class AuthRemoteDataSource @Inject constructor(
    private val authService: AuthService
) {
    /**
     * Authenticates a user with provided credentials
     *
     * @param credentials The user credentials
     * @return [Result] containing [AuthResult] if successful, or an exception if failed
     */
    suspend fun login(credentials: Credentials): Result<AuthResult> = withContext(Dispatchers.IO) {
        try {
            // Convert credentials and log the request payload for debugging
            val requestDto = AuthMapper.credentialsToDto(credentials)
            Log.d(
                "AuthRemoteDataSource",
                "Login request payload: username=${requestDto.username}, password length=${requestDto.password.length}"
            )

            // Create a hardcoded request for testing with known working credentials
            val testRequest =
                if (credentials.username == "admin@guzmanalan.com" && credentials.password == "12345") {
                    Log.d("AuthRemoteDataSource", "Using test credentials that work in Postman")
                    AuthRequestDto(
                        username = "admin@guzmanalan.com",
                        password = "12345"
                    )
                } else {
                    requestDto
                }

            val response = authService.login(testRequest)

            if (response.isSuccessful) {
                // Extract the token from the Authorization header
                val authHeader = response.headers()["Authorization"]

                if (!authHeader.isNullOrBlank()) {
                    Log.d(
                        "AuthRemoteDataSource",
                        "Found token in Authorization header: ${authHeader.take(15)}..."
                    )
                    return@withContext Result.success(
                        AuthResult(
                            token = authHeader,
                            userId = "" // Using empty string as placeholder
                        )
                    )
                }

                // Fallback to response body if header is not present
                val body = response.body()
                return@withContext if (body != null) {
                    Log.d("AuthRemoteDataSource", "Login successful with response body: $body")
                    Result.success(AuthMapper.dtoToAuthResult(body))
                } else {
                    Log.e(
                        "AuthRemoteDataSource",
                        "Login response body was null and no Authorization header found"
                    )
                    Result.failure(Exception("No token found in response"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                // Specific error handling based on HTTP status code
                val errorMessage = when (response.code()) {
                    403 -> "Invalid username or password"
                    401 -> "Unauthorized access"
                    404 -> "Service not found"
                    500 -> "Server error occurred"
                    else -> "API error ${response.code()}: $errorBody"
                }

                Log.e(
                    "AuthRemoteDataSource",
                    "Login failed: $errorMessage"
                )
                return@withContext Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("AuthRemoteDataSource", "Login exception: ${e.message}", e)
            return@withContext Result.failure(e)
        }
    }

    /**
     * Logs out the current user
     *
     * @return [Result] with success or failure
     */
    suspend fun logout(): Result<Unit> = safeApiCall {
        authService.logout()
    }

    /**
     * Helper function to handle API calls safely.
     *
     * @param apiCall The suspending function making the API call
     * @return [Result] containing the data if successful, or an exception if failed
     */
    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> = try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Response body was null"))
        } else {
            val errorBody = response.errorBody()?.string()
            // Improved error handling
            val errorMessage = when (response.code()) {
                403 -> "Invalid username or password"
                401 -> "Unauthorized access"
                else -> "API error ${response.code()}: $errorBody"
            }
            Result.failure(Exception(errorMessage))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}