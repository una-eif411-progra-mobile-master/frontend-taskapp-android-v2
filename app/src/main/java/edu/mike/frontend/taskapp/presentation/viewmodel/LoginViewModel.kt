package edu.mike.frontend.taskapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.mike.frontend.taskapp.data.repository.LoginRepository
import edu.mike.frontend.taskapp.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for handling user login state.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository, // Inject the login repository
    private val tokenManager: TokenManager // Inject the TokenManager
) : ViewModel() {

    // StateFlow to hold the login status
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn

    // StateFlow to hold the error message (for login failures)
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> get() = _loginError

    /**
     * Function to handle login logic using the LoginRepository.
     */
    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = loginRepository.login(username, password)
            result.onSuccess { (loginResponse, token) ->
                if (loginResponse != null && token != null) {
                    // Store the JWT token via the TokenManager after successful login
                    tokenManager.saveToken(token)

                    _isLoggedIn.value = true
                    _loginError.value = null
                } else {
                    _isLoggedIn.value = false
                    _loginError.value = "Login failed. Please check your credentials."
                }
            }.onFailure { exception ->
                _isLoggedIn.value = false
                _loginError.value = "An error occurred: ${exception.message}"
            }
        }
    }

    /**
     * Function to handle logout logic.
     */
    fun logout() {
        tokenManager.clearToken() // Clear the token via TokenManager
        _isLoggedIn.value = false
    }

    // Retrieve the JWT token via TokenManager
    fun getToken(): String? {
        return tokenManager.getToken()
    }
}