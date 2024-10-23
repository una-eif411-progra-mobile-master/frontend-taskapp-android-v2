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
     * @return A Pair containing the LoginResponse object and the JWT token, or null if an error occurs.
     */
    suspend fun login(username: String, password: String): Result<Pair<LoginResponse?, String?>> {
        return try {
            val response: Response<LoginResponse> =
                loginService.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                // Extract the JWT token from the header (assuming it's in the "Authorization" header)
                val token = response.headers()["Authorization"]?.replace("Bearer ", "")
                Result.success(Pair(response.body(), token))
            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}