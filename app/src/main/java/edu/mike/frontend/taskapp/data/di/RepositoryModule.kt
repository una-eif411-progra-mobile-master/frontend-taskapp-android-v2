package edu.mike.frontend.taskapp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.mike.frontend.taskapp.data.repository.TaskRepositoryImpl
import edu.mike.frontend.taskapp.domain.repository.TaskRepository
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides repository dependencies for the application.
 *
 * This module uses [Binds] to map implementation classes to their interfaces,
 * allowing Hilt to inject the correct implementation when an interface type is requested.
 * All bindings are scoped to [SingletonComponent] to ensure a single instance is shared
 * throughout the application's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds the concrete implementation [TaskRepositoryImpl] to the [TaskRepository] interface.
     *
     * @param taskRepositoryImpl The implementation instance to be provided when [TaskRepository] is requested
     * @return The bound [TaskRepository] interface
     */
    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TaskRepository
}