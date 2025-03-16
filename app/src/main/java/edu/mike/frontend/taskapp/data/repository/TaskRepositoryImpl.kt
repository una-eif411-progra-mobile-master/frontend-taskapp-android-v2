package edu.mike.frontend.taskapp.data.repository

import edu.mike.frontend.taskapp.data.datasource.TaskDataSource
import edu.mike.frontend.taskapp.data.mapper.TaskMapper
import edu.mike.frontend.taskapp.domain.error.DomainError
import edu.mike.frontend.taskapp.domain.model.Task
import edu.mike.frontend.taskapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.first
import java.io.IOException

/**
 * Implementation of [TaskRepository] that handles task data operations.
 * Provides error handling and mapping between data and domain layers.
 *
 * @property dataSource The data source for task operations
 * @property taskMapper The mapper for converting between domain and data layer task objects
 */
class TaskRepositoryImpl(
    private val dataSource: TaskDataSource,
    private val taskMapper: TaskMapper
) : TaskRepository {

    /**
     * Retrieves all tasks from the data source.
     *
     * @return [Result] containing a list of tasks if successful, or an error if the operation failed
     * @throws DomainError.NetworkError if there's a network-related issue
     * @throws DomainError.MappingError if there's an error mapping the data
     * @throws DomainError.UnknownError for unexpected errors
     */
    override suspend fun findAllTasks(): Result<List<Task>> = runCatching {
        dataSource.getTasks().first().map { taskDto ->
            taskMapper.mapToDomain(taskDto)
        }
    }.recoverCatching { throwable ->
        when (throwable) {
            is IOException -> throw DomainError.NetworkError("Failed to fetch tasks")
            is IllegalArgumentException -> throw DomainError.MappingError("Error mapping tasks")
            is DomainError -> throw throwable
            else -> throw DomainError.UnknownError
        }
    }

    /**
     * Finds a task by its ID.
     *
     * @param taskId The ID of the task to find
     * @return [Result] containing the task if found, or an error if the operation failed
     * @throws DomainError.TaskError if the task is not found
     * @throws DomainError.NetworkError if there's a network-related issue
     * @throws DomainError.MappingError if there's an error mapping the task
     */
    override suspend fun findTaskById(taskId: Long): Result<Task> = runCatching {
        val taskDto =
            dataSource.getTaskById(taskId) ?: throw DomainError.TaskError("Task not found")
        taskMapper.mapToDomain(taskDto)
    }.recoverCatching { throwable ->
        when (throwable) {
            is IOException -> throw DomainError.NetworkError("Failed to fetch task")
            is IllegalArgumentException -> throw DomainError.MappingError("Error mapping task")
            is DomainError -> throw throwable
            else -> throw DomainError.UnknownError
        }
    }

    /**
     * Saves a new task to the data source.
     *
     * @param task The task to be saved
     * @return [Result] indicating success or failure
     * @throws DomainError.NetworkError if there's a network-related issue
     * @throws DomainError.MappingError if there's an error mapping the task
     * @throws DomainError.TaskError for task-specific errors
     */
    override suspend fun saveTask(task: Task): Result<Unit> = runCatching {
        require(task.title.isNotBlank()) { "Task title cannot be empty" }
        dataSource.insertTask(taskMapper.mapToDto(task))
    }.recoverCatching { throwable ->
        when (throwable) {
            is IOException -> throw DomainError.NetworkError("Failed to save task")
            is IllegalArgumentException -> throw DomainError.MappingError("Error mapping task")
            else -> throw DomainError.TaskError("Failed to save task: ${throwable.message}")
        }
    }

    /**
     * Deletes a task from the data source.
     *
     * @param taskId The ID of the task to delete
     * @return [Result] indicating success or failure
     * @throws DomainError.TaskError if the task is not found
     * @throws DomainError.NetworkError if there's a network-related issue
     */
    override suspend fun deleteTask(taskId: Long): Result<Unit> = runCatching {
        val task = dataSource.getTaskById(taskId) ?: throw DomainError.TaskError("Task not found")
        dataSource.deleteTask(task)
    }.recoverCatching { throwable ->
        when (throwable) {
            is IOException -> throw DomainError.NetworkError("Failed to delete task")
            is DomainError -> throw throwable
            else -> throw DomainError.UnknownError
        }
    }

    /**
     * Updates an existing task in the data source.
     *
     * @param task The task to be updated
     * @return [Result] indicating success or failure
     * @throws DomainError.NetworkError if there's a network-related issue
     * @throws DomainError.MappingError if there's an error mapping the task
     * @throws DomainError.TaskError for task-specific errors
     */
    override suspend fun updateTask(task: Task): Result<Unit> = runCatching {
        dataSource.updateTask(taskMapper.mapToDto(task))
    }.recoverCatching { throwable ->
        when (throwable) {
            is IOException -> throw DomainError.NetworkError("Failed to update task")
            is IllegalArgumentException -> throw DomainError.MappingError("Error mapping task")
            else -> throw DomainError.TaskError("Failed to update task: ${throwable.message}")
        }
    }
}