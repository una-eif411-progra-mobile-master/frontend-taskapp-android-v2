package edu.mike.frontend.taskapp.presentation.ui.screens

// Add these imports at the top of your file
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import edu.mike.frontend.taskapp.R
import edu.mike.frontend.taskapp.presentation.viewmodel.LoginState
import edu.mike.frontend.taskapp.presentation.viewmodel.LoginViewModel

/**
 * Composable function for the Login screen.
 * Provides a user interface for authentication with username and password fields,
 * error handling, and loading state visualization.
 *
 * @param loginViewModel The ViewModel handling the login logic and authentication state.
 * @param onLoginSuccess Callback function to be called on successful login.
 */
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    // Collect states from the ViewModel
    val loginState by loginViewModel.loginState.collectAsState()
    val isLoading by loginViewModel.isLoading.collectAsState()

    // Local UI state
    var username by remember { mutableStateOf("admin@guzmanalan.com") } //TODO: This is for testing only
    var password by remember { mutableStateOf("12345") } //TODO: This is for testing only
    var passwordVisible by remember { mutableStateOf(false) }

    // Validation states
    val isEmailValid =
        username.isEmpty() || username.matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"))
    val isPasswordValid = password.isEmpty() || password.length >= 5

    // Navigate on successful login
    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // App logo
        Image(
            painter = painterResource(id = R.drawable.ic_task_app_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 24.dp)
        )

        Text(
            text = "Task Manager",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Show loading indicator at the top of the form when loading
        if (loginState is LoginState.Loading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = null
                )
            },
            singleLine = true,
            enabled = !isLoading,
            isError = !isEmailValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        // Email validation error
        if (!isEmailValid) {
            Text(
                text = "Please enter a valid email address",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 8.dp)
            )
        }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = null
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            singleLine = true,
            enabled = !isLoading,
            isError = !isPasswordValid,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (!isLoading && isEmailValid && isPasswordValid &&
                        username.isNotEmpty() && password.isNotEmpty()
                    ) {
                        loginViewModel.login(username, password)
                    }
                }
            )
        )

        // Password validation error
        if (!isPasswordValid) {
            Text(
                text = "Password must be at least 5 characters long",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 8.dp)
            )
        }

        when (loginState) {
            is LoginState.Error -> {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = (loginState as LoginState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    TextButton(
                        onClick = { loginViewModel.resetLoginState() },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Retry")
                    }
                }
            }

            else -> { /* Don't show anything for other states */
            }
        }

        Button(
            onClick = { loginViewModel.login(username, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(top = 8.dp),
            enabled = !isLoading && isEmailValid && isPasswordValid &&
                    username.isNotEmpty() && password.isNotEmpty()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Log In")
            }
        }

        // Option for registration or password recovery
        TextButton(
            onClick = { /* Handle registration or password recovery */ },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Don't have an account? Sign up")
        }
    }
}