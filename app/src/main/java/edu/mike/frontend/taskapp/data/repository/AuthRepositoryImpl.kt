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
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authPreferences: AuthPreferences
) : AuthRepository {

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