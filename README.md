# TaskApp - Android CLEAN Architecture Example

## Overview

TaskApp is a demonstration project for computer science students to learn CLEAN Architecture in
Android development. This project implements a task management application using modern Android
development practices.

## Architecture

The project follows CLEAN architecture principles with three main layers:

### Domain Layer

- Contains business models (`Task`, `Status`, `Priority`)
- Defines repository interfaces (`TaskRepository`)
- Implements domain-specific error handling (`DomainError`)

### Data Layer

- Implements repositories (`TaskRepositoryImpl`)
- Uses data sources (`TaskDataSource`) for data access
- Contains mappers for DTO to domain model conversion
- Handles exceptions with appropriate error mapping

### Presentation Layer

- Uses ViewModel pattern (`TaskViewModel`)
- Manages UI state
- Handles user interactions

## Key Features

- Task creation, retrieval, updating, and deletion
- Error handling with domain-specific error types
- Asynchronous operations with coroutines
- Flow-based reactive data handling
- Comprehensive test coverage

## Tech Stack

- Kotlin
- Jetpack Compose UI
- Coroutines for asynchronous programming
- Flow for reactive streams
- Mockk for test mocking
- Gradle build system

## Learning Objectives

- Understanding separation of concerns in mobile applications
- Implementing repository pattern
- Error handling across architectural layers
- Unit testing ViewModels and repositories
- Managing state in Android applications

## Getting Started

1. Clone the repository
2. Open in Android Studio
3. Build and run the application

## Testing

The project includes comprehensive tests demonstrating:

- ViewModel test patterns with coroutines
- Repository testing with mocked data sources
- Error handling verification

## Best Practices Demonstrated

- Single Responsibility Principle
- Interface segregation
- Result-based error handling
- Coroutine test practices with `TestDispatcher`