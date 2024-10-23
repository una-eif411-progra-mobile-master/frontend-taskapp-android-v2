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

## Project Setup

### Prerequisites

- Android Studio
- JDK 17
- Kotlin 1.9.x

### Build Variants

The app supports three different environments:

1. **devLocal**
    - Development environment using localhost
    - BASE_URL: `http://10.0.2.2:8080/v1/`
    - Used for local development and testing

2. **devHeroku**
    - Development environment using Heroku
    - BASE_URL: `https://task-app-v01.herokuapp.com/v1/`
    - Used for development and testing with remote server

3. **prod**
    - Production environment
    - BASE_URL: `https://production.example.com/v1/`
    - Used for production releases

To switch between variants in Android Studio:

1. Go to View > Tool Windows > Build Variants
2. Select the desired build variant from the list

## Project Structure

### Key Components

#### Network Security

- `network_security_config.xml`: Configures network security settings
- Allows cleartext traffic for localhost and emulator testing

#### Authentication

- `LoginService`: Handles API calls for authentication
- `TokenManager`: Manages JWT token storage and retrieval
- `LoginRepository`: Manages login business logic
- `LoginViewModel`: Handles UI state and login operations

#### Networking

- `NetworkModule`: Dagger/Hilt module for networking setup
- Includes interceptors for token management
- Configures Retrofit with environment-specific base URLs

## Implementation Details

### Network Security Configuration

```xml

<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">localhost</domain>
        <domain includeSubdomains="true">10.0.2.2</domain>
    </domain-config>
</network-security-config>
```

### Authentication Flow

1. User enters credentials
2. LoginViewModel processes login request
3. JWT token is stored securely using TokenManager
4. Subsequent requests include token in Authorization header

## Development Guidelines

### Adding New Features

1. Create necessary data models
2. Implement service interface
3. Create repository class
4. Update ViewModel
5. Create UI components using Compose

### Best Practices

- Use dependency injection with Hilt
- Follow MVVM architecture patterns
- Implement proper error handling
- Use coroutines for asynchronous operations
- Keep UI state in ViewModels