package edu.mike.frontend.taskapp.domain.repository

import edu.mike.frontend.taskapp.domain.model.Task

/**
 * Repository interface for task-related operations.
 * Defines methods for retrieving, creating, updating and deleting tasks.
 */
interface TaskRepository {
    /**
     * Retrieves all tasks.
     *
     * @return [Result] wrapping a list of [Task] entities
     */
    suspend fun findAllTasks(): Result<List<Task>>

    /**
     * Retrieves a specific task by its ID.
     *
     * @param taskId The unique identifier of the task
     * @return [Result] wrapping the requested [Task]
     */
    suspend fun findTaskById(taskId: Long): Result<Task>

    /**
     * Creates a new task.
     *
     * @param task The task to be created
     * @return [Result] wrapping the created [Task]
     */
    suspend fun createTask(task: Task): Result<Task>

    /**
     * Updates an existing task.
     *
     * @param task The task to be updated with new data
     * @return [Result] wrapping the updated [Task]
     */
    suspend fun updateTask(task: Task): Result<Task>

    /**
     * Deletes a task by its ID.
     *
     * @param taskId The unique identifier of the task to delete
     * @return [Result] with success or failure
     */
    suspend fun deleteTask(taskId: Long): Result<Unit>
}