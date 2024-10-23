package edu.mike.frontend.taskapp.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.mike.frontend.taskapp.presentation.viewmodel.LoginViewModel

/**
 * Composable function for the Login screen.
 *
 * @param loginViewModel The ViewModel handling the login logic.
 * @param onLoginSuccess Callback function to be called on successful login.
 */
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    // State variables for username, password
    var username by remember { mutableStateOf("admin@guzmanalan.com") } // Set default username here
    var password by remember { mutableStateOf("12345") }

    // Observe the login error and login state
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsStateWithLifecycle()
    val loginError by loginViewModel.loginError.collectAsStateWithLifecycle()

    // Center the login form
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Username Label and Input Field
            Text(text = "Username", modifier = Modifier.align(Alignment.Start))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Enter your username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password Label and Input Field
            Text(text = "Password", modifier = Modifier.align(Alignment.Start))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Enter your password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            Button(
                onClick = {
                    loginViewModel.login(username, password)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }

            // Show error message if login fails
            if (loginError != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    loginError!!,
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    // Navigate to the next screen on successful login
    if (isLoggedIn) {
        onLoginSuccess()
    }
}