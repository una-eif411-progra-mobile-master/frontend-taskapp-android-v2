// app/src/main/java/edu/mike/frontend/taskapp/data/remote/AuthRemoteDataSource.kt
package edu.mike.frontend.taskapp.data.remote

import edu.mike.frontend.taskapp.data.mapper.AuthMapper
import edu.mike.frontend.taskapp.data.remote.api.AuthService
import edu.mike.frontend.taskapp.domain.model.AuthResult
import edu.mike.frontend.taskapp.domain.model.Credentials
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
    suspend fun login(credentials: Credentials): Result<AuthResult> = safeApiCall {
        val requestDto = AuthMapper.credentialsToDto(credentials)
        authService.login(requestDto)
    }.map { AuthMapper.dtoToAuthResult(it) }

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