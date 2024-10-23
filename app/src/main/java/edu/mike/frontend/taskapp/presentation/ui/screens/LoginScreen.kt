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
    // State variables for username, password, and login error
    var username by remember { mutableStateOf("user") }
    var password by remember { mutableStateOf("password") }
    var loginError by remember { mutableStateOf(false) }

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

            // Spacer for better spacing
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
                    val isValid = loginViewModel.validateCredentials(username, password)
                    if (isValid) {
                        loginError = false
                        onLoginSuccess()
                    } else {
                        loginError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }

            // Show error message if login fails
            if (loginError) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Invalid credentials. Please try again.",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}