package edu.mike.frontend.taskapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for handling user login state.
 */
class LoginViewModel : ViewModel() {

    // StateFlow to hold the login status
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn

    /**
     * Function to handle login logic.
     * You can replace this logic with actual login API call or authentication logic.
     */
    fun login(username: String, password: String) {
        // Simulate login process (replace with real login logic)
        viewModelScope.launch {
            // Simulate login success
            _isLoggedIn.value = true
        }
    }

    /**
     * Function to handle logout logic.
     */
    fun logout() {
        _isLoggedIn.value = false
    }

    /**
     * Function to validate the user credentials.
     *
     * @param username The input username.
     * @param password The input password.
     * @return A boolean indicating whether the credentials are valid.
     */
    fun validateCredentials(username: String, password: String): Boolean {
        // Example credentials validation logic (this is just a simple example).
        return if (username == "user" && password == "password") {
            _isLoggedIn.value = true
            true
        } else {
            _isLoggedIn.value = false
            false
        }
    }
}