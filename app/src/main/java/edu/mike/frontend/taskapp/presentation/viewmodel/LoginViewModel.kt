package edu.mike.frontend.taskapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.mike.frontend.taskapp.domain.repository.AuthRepository
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
     * Loading state during authentication process
     */
    data object Loading : LoginState()

    /**
     * Success state after successful authentication
     */
    data object Success : LoginState()

    /**
     * Error state containing the error message when authentication fails
     *
     * @property message The error message describing the failure reason
     */
    data class Error(val message: String) : LoginState()
}

/**
 * Data class representing the complete UI state for the login screen.
 *
 * @property isLoading Whether a login operation is in progress
 * @property isLoggedIn Whether the user is currently logged in
 * @property errorMessage Error message to display, if any
 * @property username Current username input value
 * @property password Current password input value
 */
data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val errorMessage: String? = null,
    val username: String = "",
    val password: String = ""
)

/**
 * ViewModel responsible for managing login-related UI state and business logic.
 *
 * Handles authentication operations through the AuthRepository, manages login state,
 * and provides functions for login, logout, and state management.
 *
 * @property authRepository Repository handling authentication operations with the backend
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Initial)
    val loginState = _loginState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        checkAuthenticationStatus()
    }

    /**
     * Checks if user is already authenticated when ViewModel is initialized
     */
    private fun checkAuthenticationStatus() {
        viewModelScope.launch {
            authRepository.isAuthenticated()
                .onSuccess { authenticated ->
                    _isLoggedIn.value = authenticated
                    Log.d("LoginViewModel", "Authentication status checked: $authenticated")
                }
                .onFailure {
                    Log.e("LoginViewModel", "Error checking authentication status: ${it.message}")
                }
        }
    }

    /**
     * Attempts to log in with the provided credentials using the auth repository.
     *
     * Performs basic input validation before making the network request.
     * Updates state flows based on the result of the authentication attempt.
     *
     * @param username The user's username or email
     * @param password The user's password
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _loginState.value = LoginState.Loading

            // Validate inputs before attempting network call
            if (!isValidEmail(username)) {
                _loginState.value = LoginState.Error("Invalid email format")
                _isLoading.value = false
                return@launch
            }

            if (password.length < 5) {
                _loginState.value = LoginState.Error("Password must be at least 5 characters")
                _isLoading.value = false
                return@launch
            }

            authRepository.login(username, password)
                .onSuccess {
                    _loginState.value = LoginState.Success
                    _isLoggedIn.value = true
                    Log.d("LoginViewModel", "Login successful for user: $username")
                }
                .onFailure { exception ->
                    _loginState.value =
                        LoginState.Error(exception.message ?: "Authentication failed")
                    Log.e(
                        "LoginViewModel",
                        "Login failed for user: $username, error: ${exception.message}"
                    )
                }

            _isLoading.value = false
        }
    }

    /**
     * Validates if the provided string is a valid email address.
     *
     * @param email The email address to validate
     * @return True if the email is valid, false otherwise
     */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Logs the user out using the auth repository.
     * Clears authentication data and resets UI state.
     */
    fun logout() {
        viewModelScope.launch {
            _isLoading.value = true

            authRepository.logout()
                .onSuccess {
                    _isLoggedIn.value = false
                    _loginState.value = LoginState.Initial
                    Log.d("LoginViewModel", "User logged out successfully")
                }
                .onFailure { exception ->
                    Log.e("LoginViewModel", "Logout failed: ${exception.message}")
                    // Even if remote logout fails, we should reset the UI state
                    _isLoggedIn.value = false
                    _loginState.value = LoginState.Initial
                }

            _isLoading.value = false
        }
    }

    /**
     * Resets any error state to allow for retry attempts.
     * This returns the login flow to its initial state.
     */
    fun resetLoginState() {
        _loginState.value = LoginState.Initial
    }
}