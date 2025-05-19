package edu.mike.frontend.taskapp.data.repository

import android.util.Log
import edu.mike.frontend.taskapp.data.local.AuthPreferences
import edu.mike.frontend.taskapp.data.remote.AuthRemoteDataSource
import edu.mike.frontend.taskapp.domain.model.Credentials
import edu.mike.frontend.taskapp.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of [AuthRepository] that manages authentication using
 * a remote service and local session storage.
 *
 * This class follows the Repository pattern, acting as a mediator between different
 * data sources (remote API and local preferences storage) and the rest of the application.
 * It handles authentication-related operations such as login, logout, and checking
 * authentication status.
 *
 * @property authRemoteDataSource The data source for remote authentication operations
 * @property authPreferences Local storage for authentication data (tokens, username)
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authPreferences: AuthPreferences
) : AuthRepository {

    /**
     * Authenticates a user with the provided credentials.
     *
     * This function:
     * 1. Creates a credentials object from the provided username and password
     * 2. Attempts to authenticate with the remote service
     * 3. On success, stores the authentication token and username locally
     * 4. Handles and logs any errors that occur during the process
     *
     * @param username The user's username or email
     * @param password The user's password
     * @return A [Result] containing [Unit] if successful, or an error if authentication failed
     */
    override suspend fun login(username: String, password: String): Result<Unit> {
        return try {
            Log.d("AuthRepositoryImpl", "Login attempt for user: $username")
            val credentials = Credentials(username, password)

            authRemoteDataSource.login(credentials)
                .onSuccess { authResult ->
                    val token = authResult.token
                    if (token.isNotBlank()) {
                        Log.d(
                            "AuthRepositoryImpl",
                            "Login successful, token: ${token.take(10)}..."
                        )
                        // Save auth state to preferences
                        authPreferences.saveAuthToken(token)
                        authPreferences.saveUsername(username)
                    } else {
                        Log.e("AuthRepositoryImpl", "Received empty token in auth result")
                    }
                }
                .onFailure { error ->
                    Log.e("AuthRepositoryImpl", "Login failed: ${error.message}")
                }
                .map { }
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Login exception: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Logs out the current user from both remote service and local storage.
     *
     * This function:
     * 1. Attempts to logout from the remote service (if applicable)
     * 2. Regardless of remote logout success, clears local authentication data
     * 3. Ensures the user is fully logged out even if remote operation fails
     *
     * This approach follows the principle of resilience, ensuring that local state
     * remains consistent even when network operations fail.
     *
     * @return A [Result] containing [Unit] if successful, or an error if logout failed
     */
    override suspend fun logout(): Result<Unit> {
        return try {
            Log.d("AuthRepositoryImpl", "Attempting to logout")
            // Call remote logout if needed
            authRemoteDataSource.logout()
                .onSuccess {
                    Log.d("AuthRepositoryImpl", "Remote logout successful")
                }
                .onFailure { error ->
                    Log.e("AuthRepositoryImpl", "Remote logout failed: ${error.message}")
                    // Continue with local logout even if remote fails
                }

            // Clear local auth state regardless of remote result
            authPreferences.clearAuthData()
            Log.d("AuthRepositoryImpl", "Local auth data cleared")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Logout exception: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Checks if the user is currently authenticated.
     *
     * Authentication is determined by the presence of a non-empty auth token in local storage.
     * This is a quick check that doesn't involve network operations, making it suitable for
     * frequent calls (e.g., in UI state management).
     *
     * @return A [Result] containing a boolean indicating authentication status
     */
    override suspend fun isAuthenticated(): Result<Boolean> {
        return try {
            val token = authPreferences.getAuthToken()
            val isAuthenticated = token != null && token.isNotEmpty()
            Log.d("AuthRepositoryImpl", "Authentication check: $isAuthenticated")
            Result.success(isAuthenticated)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Auth check exception: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Retrieves the username of the currently authenticated user.
     *
     * This function:
     * 1. Retrieves the stored username from local preferences
     * 2. Returns null if no user is currently authenticated
     *
     * This method is useful for displaying user information in the UI
     * or for attaching user identity to operations without requiring re-authentication.
     *
     * @return A [Result] containing the current username or null if not authenticated
     */
    override suspend fun getCurrentUser(): Result<String?> {
        return try {
            val username = authPreferences.getUsername()
            Log.d("AuthRepositoryImpl", "Current user: $username")
            Result.success(username)
        } catch (e: Exception) {
            Log.e("AuthRepositoryImpl", "Get current user exception: ${e.message}", e)
            Result.failure(e)
        }
    }
}