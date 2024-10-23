package edu.mike.frontend.taskapp.data.repository

import edu.mike.frontend.taskapp.data.model.LoginRequest
import edu.mike.frontend.taskapp.data.model.LoginResponse
import edu.mike.frontend.taskapp.data.network.LoginService
import retrofit2.Response
import javax.inject.Inject

/**
 * Repository class for managing login data operations.
 *
 * @property loginService The LoginService instance for network operations.
 */
class LoginRepository @Inject constructor(
    private val loginService: LoginService
) {
    /**
     * Logs in a user with the provided credentials.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The LoginResponse object or null if an error occurs.
     */
    suspend fun login(username: String, password: String): Result<LoginResponse?> {
        return try {
            val response: Response<LoginResponse> =
                loginService.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}