package edu.mike.frontend.taskapp.data.local

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "auth_preferences")

/**
 * Manages authentication data persistence using DataStore preferences.
 *
 * This singleton class provides methods to store, retrieve, and manage authentication-related
 * information such as JWT tokens and usernames. It uses Android's DataStore preferences as the
 * underlying storage mechanism for secure and efficient data persistence.
 *
 * All operations are suspend functions to ensure they're called from a coroutine context
 * as DataStore operations are asynchronous.
 */
@Singleton
class AuthPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore
    private val TAG = "AuthPreferences"

    /**
     * Keys used for storing preference values in DataStore.
     */
    private object PreferencesKeys {
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val USERNAME = stringPreferencesKey("username")
    }

    /**
     * Saves the provided authentication token to persistent storage.
     *
     * If the token starts with "Bearer ", it will extract and store only the actual token.
     * Blank tokens are ignored and not saved.
     *
     * @param token The authentication token to save
     */
    suspend fun saveAuthToken(token: String) {
        try {
            // Check if token starts with "Bearer " and extract the actual token
            val actualToken = when {
                token.startsWith("Bearer ", ignoreCase = true) -> token.substring(7)
                token.isBlank() -> {
                    Log.w(TAG, "Attempting to save blank token - ignoring")
                    return
                }

                else -> token
            }

            dataStore.edit { preferences ->
                preferences[PreferencesKeys.AUTH_TOKEN] = actualToken
            }

            val saveSuccessful = getAuthToken() == actualToken
            Log.d(
                TAG,
                "Token saved successfully: $saveSuccessful (first 10 chars: ${actualToken.take(10)}...)"
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error saving auth token: ${e.message}", e)
        }
    }

    /**
     * Saves the username to persistent storage.
     *
     * @param username The username to save
     */
    suspend fun saveUsername(username: String) {
        try {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.USERNAME] = username
            }
            Log.d(TAG, "Username saved: $username")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving username: ${e.message}", e)
        }
    }

    /**
     * Retrieves the stored authentication token.
     *
     * @return The stored token or null if not found or an error occurs
     */
    suspend fun getAuthToken(): String? {
        return try {
            val token = dataStore.data.map { preferences ->
                preferences[PreferencesKeys.AUTH_TOKEN]
            }.first()

            if (token.isNullOrBlank()) {
                Log.w(TAG, "Retrieved token is null or blank")
            } else {
                Log.d(TAG, "Retrieved token successfully (first 10 chars: ${token.take(10)}...)")
            }

            token
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving auth token: ${e.message}", e)
            null
        }
    }

    /**
     * Retrieves the stored token with "Bearer " prefix for use in API requests.
     *
     * @return The formatted token with "Bearer " prefix or null if the token is not available
     */
    suspend fun getFormattedAuthToken(): String? {
        val token = getAuthToken()
        return if (!token.isNullOrBlank()) {
            "Bearer $token"
        } else {
            null
        }
    }

    /**
     * Retrieves the stored username.
     *
     * @return The stored username or null if not found or an error occurs
     */
    suspend fun getUsername(): String? {
        return try {
            dataStore.data.map { preferences ->
                preferences[PreferencesKeys.USERNAME]
            }.first()
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving username: ${e.message}", e)
            null
        }
    }

    /**
     * Clears all stored authentication data (token and username).
     *
     * This method is typically called during logout.
     */
    suspend fun clearAuthData() {
        try {
            dataStore.edit { preferences ->
                preferences.remove(PreferencesKeys.AUTH_TOKEN)
                preferences.remove(PreferencesKeys.USERNAME)
            }
            Log.d(TAG, "Auth data cleared")
        } catch (e: Exception) {
            Log.e(TAG, "Error clearing auth data: ${e.message}", e)
        }
    }

    /**
     * Checks if the user is currently authenticated.
     *
     * Authentication is determined by the presence of a non-empty token.
     *
     * @return true if the user is authenticated, false otherwise
     */
    suspend fun isAuthenticated(): Boolean {
        val token = getAuthToken()
        val isAuth = !token.isNullOrBlank()
        Log.d(TAG, "Checking authentication status: $isAuth")
        return isAuth
    }
}