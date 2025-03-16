package edu.mike.frontend.taskapp.domain.repository

import edu.mike.frontend.taskapp.domain.model.Task

/**
 * This interface represents the TaskRepository.
 */
interface TaskRepository {
    suspend fun findAllTasks(): Result<List<Task>>
    suspend fun findTaskById(taskId: Long): Result<Task>
    suspend fun saveTask(task: Task): Result<Unit>
    suspend fun deleteTask(taskId: Long): Result<Unit>
    suspend fun updateTask(task: Task): Result<Unit>
}