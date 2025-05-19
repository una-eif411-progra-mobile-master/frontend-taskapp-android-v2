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
                response.body()?.let {
                    Log.d("AuthRemoteDataSource", "Login successful with response: $it")
                    Result.success(AuthMapper.dtoToAuthResult(it))
                } ?: run {
                    Log.e("AuthRemoteDataSource", "Login response body was null")
                    Result.failure(Exception("Response body was null"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e(
                    "AuthRemoteDataSource",
                    "Login failed with code ${response.code()}: $errorBody"
                )
                Result.failure(Exception("API error ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            Log.e("AuthRemoteDataSource", "Login exception: ${e.message}", e)
            Result.failure(e)
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
            Result.failure(Exception("API error ${response.code()}: $errorBody"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}