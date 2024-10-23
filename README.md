# Android Task App with JWT Authentication

## Overview

An Android application built with Jetpack Compose that implements JWT (JSON Web Token)
authentication and manages tasks. The app supports multiple environments through product flavors and
follows modern Android development practices.

## Features

- JWT Authentication
- Task Management
- Multiple Environment Support
- Modern UI with Jetpack Compose

## Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Authentication**: JWT
- **Dependency Injection**: Hilt
- **Networking**: Retrofit
- **Architecture**: MVVM

## Table of Contents

1. [Update `network_security_config.xml`](#1-update-network_security_configxml)
2. [Update `AndroidManifest.xml`](#2-update-androidmanifestxml)
3. [Create and Update `LoginService`](#3-create-and-update-loginservice)
4. [Update `NetworkModule`](#4-update-networkmodule)
5. [Create `LoginRepository`](#5-create-loginrepository)
6. [Create `TokenManager`](#6-create-tokenmanager)
7. [Create `LoginViewModel`](#7-create-loginviewmodel)
8. [Update `LoginScreen`](#8-update-loginscreen)
9. [Update `BottomNavigationBar`](#9-update-bottomnavigationbar)
10. [Update `MainLayout`](#10-update-mainlayout)
11. [Add Product Flavors in `build.gradle.kts`](#11-add-product-flavors-in-buildgradlekts)

---

## 1. Update `network_security_config.xml`

Ensure the app can connect to localhost or Heroku during development. Create or update
`res/xml/network_security_config.xml` to allow cleartext traffic:

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">localhost</domain>
        <domain includeSubdomains="true">10.0.2.2</domain>
    </domain-config>
</network-security-config>
```

## 2. Update `AndroidManifest.xml`

Add the network security configuration to your app’s `AndroidManifest.xml`:

```xml

<application android:networkSecurityConfig="@xml/network_security_config"...></application>
```

## 3. Create and Update `LoginService`

Create an interface for login-related API calls in `LoginService`:

```kotlin
package edu.mike.frontend.taskapp.data.network

import edu.mike.frontend.taskapp.data.model.LoginRequest
import edu.mike.frontend.taskapp.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/users/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
```

## 4. Update `NetworkModule`

Modify your Dagger `NetworkModule` to add token handling via `TokenManager` and configure the base
URL for each environment.

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(tokenManager: TokenManager): Interceptor {
        return Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            tokenManager.getToken()?.let { token ->
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(requestBuilder.build())
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }
}
```

## 5. Create `LoginRepository`

Create a repository to handle login requests and store the JWT token. Add this class in
`edu.mike.frontend.taskapp.data.repository`:

```kotlin
class LoginRepository @Inject constructor(
    private val loginService: LoginService,
    private val tokenManager: TokenManager
) {
    suspend fun login(username: String, password: String): Result<LoginResponse?> {
        return try {
            val response = loginService.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                response.headers()["Authorization"]?.let { tokenManager.saveToken(it) }
                Result.success(response.body())
            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

## 6. Create `TokenManager`

Create a `TokenManager` class to store and retrieve the JWT token in shared preferences. Place this
in `edu.mike.frontend.taskapp.utils`:

```kotlin
class TokenManager @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("jwt_token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("jwt_token", null)
    }

    fun clearToken() {
        prefs.edit().remove("jwt_token").apply()
    }
}
```

## 7. Create `LoginViewModel`

Update the `LoginViewModel` to handle login logic and store the JWT token using `TokenManager`.

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = loginRepository.login(username, password)
            result.onSuccess {
                _isLoggedIn.value = true
            }.onFailure {
                _isLoggedIn.value = false
            }
        }
    }

    fun logout() {
        tokenManager.clearToken()
        _isLoggedIn.value = false
    }
}
```

## 8. Update `LoginScreen`

Update the `LoginScreen` to initiate the login process through the `LoginViewModel`.

```kotlin
@Composable
fun LoginScreen(loginViewModel: LoginViewModel, onLoginSuccess: () -> Unit) {
    var username by remember { mutableStateOf("admin@guzmanalan.com") }
    var password by remember { mutableStateOf("12345") }

    Button(onClick = {
        loginViewModel.login(username, password)
        if (loginViewModel.isLoggedIn.collectAsState().value) {
            onLoginSuccess()
        }
    }) {
        Text("Login")
    }
}
```

## 9. Update `BottomNavigationBar`

Ensure that the logout action triggers the `LoginViewModel.logout()` function.

```kotlin
@Composable
fun BottomNavigationBar(
    navController: NavController,
    taskViewModel: TaskViewModel,
    loginViewModel: LoginViewModel
) {
    // Add Logout logic in the BottomNavigationBar component
    items.forEach { item ->
        NavigationBarItem(
            onClick = {
                if (item is BottomNavItem.Logout) {
                    loginViewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                } else {
                    navController.navigate(item.route)
                }
            }
        )
    }
}
```

## 10. Update `MainLayout`

Use the `isLoggedIn` state to determine when to show the `TaskListScreen` after login:

```kotlin
@Composable
fun MainLayout(
    navController: NavController,
    loginViewModel: LoginViewModel,
    taskViewModel: TaskViewModel
) {
    if (loginViewModel.isLoggedIn.collectAsState().value) {
        TaskListScreen(navController, taskViewModel)
    } else {
        LoginScreen(loginViewModel, onLoginSuccess = {
            navController.navigate("taskList")
        })
    }
}
```

## 11. Add Product Flavors in `build.gradle.kts`

Configure different product flavors for development and production in `build.gradle.kts`.

```kotlin
android {
    flavorDimensions.add("environment")

    productFlavors {
        create("devLocal") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/v1/\"")
        }
        create("devHeroku") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"https://task-app-v01.herokuapp.com/v1/\"")
        }
        create("prod") {
            dimension = "environment"
            buildConfigField("String", "BASE_URL", "\"https://production.example.com/v1/\"")
        }
    }
}
```

### Switching Between Flavors

In Android Studio:

1. Go to `View` -> `Tool Windows` -> `Build Variants`.
2. Select either `devLocal`, `devHeroku`, or `prod` to use the corresponding base URL for API
   requests.

---