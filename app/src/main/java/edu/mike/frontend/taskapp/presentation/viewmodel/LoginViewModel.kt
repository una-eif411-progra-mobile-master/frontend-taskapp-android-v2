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
    data object Initial : LoginState()
    data object Loading : LoginState()
    data object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

/**
 * ViewModel responsible for managing login-related UI state and business logic.
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
                }
                .onFailure {
                    Log.e("LoginViewModel", "Error checking authentication status: ${it.message}")
                }
        }
    }

    /**
     * Attempts to log in with the provided credentials using the auth repository.
     *
     * @param username The user's username
     * @param password The user's password
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _loginState.value = LoginState.Loading

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
     * Logs the user out using the auth repository.
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
     */
    fun resetLoginState() {
        _loginState.value = LoginState.Initial
    }
}