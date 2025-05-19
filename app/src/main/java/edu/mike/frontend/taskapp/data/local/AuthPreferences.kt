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
 * Class that manages authentication data in local storage.
 */
@Singleton
class AuthPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore
    private val TAG = "AuthPreferences"

    private object PreferencesKeys {
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val USERNAME = stringPreferencesKey("username")
    }

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

    suspend fun getFormattedAuthToken(): String? {
        val token = getAuthToken()
        return if (!token.isNullOrBlank()) {
            "Bearer $token"
        } else {
            null
        }
    }

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

    suspend fun isAuthenticated(): Boolean {
        val token = getAuthToken()
        val isAuth = !token.isNullOrBlank()
        Log.d(TAG, "Checking authentication status: $isAuth")
        return isAuth
    }
}