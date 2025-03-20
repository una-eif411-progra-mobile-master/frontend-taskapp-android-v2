package edu.mike.frontend.taskapp.data.repository

import android.util.Log
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
     * Companion object for the repository.
     * Contains a tag for logging purposes.
     */
    companion object {
        private const val TAG = "TaskRepository"
    }

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

        // Log the error before throwing
        Log.e(TAG, "Failed to fetch tasks", throwable)

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

        // Log the error before throwing
        Log.e(TAG, "Failed to fetch task with ID: $taskId", throwable)
        when (throwable) {
            is IOException -> throw DomainError.NetworkError("Failed to fetch task")
            is IllegalArgumentException -> throw DomainError.MappingError("Error mapping task")
            is DomainError -> throw throwable
            else -> throw DomainError.UnknownError
        }
    }
}