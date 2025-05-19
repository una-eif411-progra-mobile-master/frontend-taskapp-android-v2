package edu.mike.frontend.taskapp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.mike.frontend.taskapp.data.repository.AuthRepositoryImpl
import edu.mike.frontend.taskapp.domain.repository.AuthRepository
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides authentication-related dependencies for the application.
 *
 * This module uses [Binds] to map implementation classes to their interfaces,
 * allowing Hilt to inject the correct implementation when an interface type is requested.
 * All bindings are scoped to [SingletonComponent] to ensure a single instance is shared
 * throughout the application's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}