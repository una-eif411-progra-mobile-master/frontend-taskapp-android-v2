package edu.mike.frontend.taskapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Sealed class representing the various states of login operations.
 */
sealed class LoginState {
    /**
     * Initial state before any login attempt
     */
    data object Initial : LoginState()

    /**
     * Indicates a login operation is in progress
     */
    data object Loading : LoginState()

    /**
     * Contains successful login data
     */
    data object Success : LoginState()

    /**
     * Represents an error condition during login
     * @property message The error message describing what went wrong
     */
    data class Error(val message: String) : LoginState()
}

/**
 * ViewModel responsible for managing login-related UI state and business logic.
 */
@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    // Login state
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)

    /**
     * Flow representing the current state of login operations
     * (loading, success, error, etc.)
     */
    val loginState = _loginState.asStateFlow()

    // Loading state
    private val _isLoading = MutableStateFlow(false)

    /**
     * Flow indicating whether a login operation is in progress
     */
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // User authentication state
    private val _isLoggedIn = MutableStateFlow(false)

    /**
     * Flow indicating whether the user is currently logged in
     */
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    /**
     * Attempts to log in with the provided credentials.
     * Updates UI state to reflect loading, success, or error conditions.
     *
     * @param username The user's username
     * @param password The user's password
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _loginState.value = LoginState.Loading

            try {
                // Simulate network delay
                kotlinx.coroutines.delay(1000)

                if (validateCredentials(username, password)) {
                    _loginState.value = LoginState.Success
                    _isLoggedIn.value = true
                    Log.d("LoginViewModel", "Login successful for user: $username")
                } else {
                    _loginState.value = LoginState.Error("Invalid username or password")
                    Log.e("LoginViewModel", "Login failed for user: $username")
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Unknown error occurred")
                Log.e("LoginViewModel", "Exception during login: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Validates the provided credentials.
     * In a real app, this would connect to your domain layer authentication use case.
     *
     * @param username The username to validate
     * @param password The password to validate
     * @return True if credentials are valid, false otherwise
     */
    private fun validateCredentials(username: String, password: String): Boolean {
        // Simple validation for demonstration purposes
        // In a real app, you would use a repository/usecase to validate against a backend
        return username.isNotEmpty() && password.length >= 4
    }

    /**
     * Logs the user out and resets the login state.
     */
    fun logout() {
        _isLoggedIn.value = false
        _loginState.value = LoginState.Initial
        Log.d("LoginViewModel", "User logged out")
    }

    /**
     * Resets any error state to allow for retry attempts.
     */
    fun resetLoginState() {
        _loginState.value = LoginState.Initial
    }
}