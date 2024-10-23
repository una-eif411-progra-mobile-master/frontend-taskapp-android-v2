package edu.mike.frontend.taskapp.utils

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TokenManager is responsible for storing and retrieving the JWT token
 * for authenticated API requests.
 */
@Singleton
class TokenManager @Inject constructor(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * Saves the JWT token to SharedPreferences.
     *
     * @param token The JWT token to be saved.
     */
    fun saveToken(token: String) {
        preferences.edit().putString(KEY_TOKEN, token).apply()
    }

    /**
     * Retrieves the JWT token from SharedPreferences.
     *
     * @return The JWT token, or null if not found.
     */
    fun getToken(): String? {
        return preferences.getString(KEY_TOKEN, null)
    }

    /**
     * Clears the JWT token from SharedPreferences.
     */
    fun clearToken() {
        preferences.edit().remove(KEY_TOKEN).apply()
    }

    companion object {
        private const val PREFS_NAME = "token_prefs"
        private const val KEY_TOKEN = "jwt_token"
    }
}