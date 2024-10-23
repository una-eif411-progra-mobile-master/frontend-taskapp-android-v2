package edu.mike.frontend.taskapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.mike.frontend.taskapp.data.model.LoginRequest
import edu.mike.frontend.taskapp.data.network.LoginService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for handling user login state.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginService: LoginService // Inject the login service
) : ViewModel() {

    // StateFlow to hold the login status
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn

    // StateFlow to hold the error message (for login failurahhhes)
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> get() = _loginError

    /**
     * Function to handle login logic using LoginRequest object.
     */
    fun login(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)
        viewModelScope.launch {
            try {
                val response = loginService.login(loginRequest)
                if (response.isSuccessful) {
                    _isLoggedIn.value = true
                    _loginError.value = null
                } else {
                    _isLoggedIn.value = false
                    _loginError.value = "Login failed. Please check your credentials."
                }
            } catch (e: Exception) {
                _isLoggedIn.value = false
                _loginError.value = "An error occurred: ${e.message}"
            }
        }
    }

    /**
     * Function to handle logout logic.
     */
    fun logout() {
        _isLoggedIn.value = false
    }
}