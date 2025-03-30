package edu.mike.frontend.taskapp.data.repository

import edu.mike.frontend.taskapp.data.mapper.TaskMapper
import edu.mike.frontend.taskapp.data.remote.TaskRemoteDataSource
import edu.mike.frontend.taskapp.domain.model.Task
import edu.mike.frontend.taskapp.domain.repository.TaskRepository
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Implementation of [TaskRepository] that handles task data operations.
 * This repository acts as a single source of truth for task data,
 * coordinating between remote data sources and mapping DTOs to domain models.
 *
 * @property remoteDataSource Data source for remote task operations
 * @property taskMapper Mapper for converting between DTO and domain models
 */
class TaskRepositoryImpl @Inject constructor(
    private val remoteDataSource: TaskRemoteDataSource,
    private val taskMapper: TaskMapper
) : TaskRepository {

    /**
     * Retrieves all tasks from the remote data source.
     *
     * @return [Result] wrapping a list of [Task] entities if successful,
     * or an exception if the operation failed
     */
    override suspend fun findAllTasks(): Result<List<Task>> {
        return try {
            remoteDataSource.getAllTasks().map { taskDtos ->
                taskMapper.mapToDomainList(taskDtos)
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Network error: Cannot connect to server. Please check your internet connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching tasks: ${e.message}"))
        }
    }

    /**
     * Retrieves a specific task by its ID.
     *
     * @param taskId The unique identifier of the task
     * @return [Result] wrapping the requested [Task] if successful,
     * or an exception if the operation failed or task was not found
     */
    override suspend fun findTaskById(taskId: Long): Result<Task> =
        remoteDataSource.getTaskById(taskId).map { taskDto ->
            taskMapper.mapToDomain(taskDto)
        }

    /**
     * Creates a new task.
     *
     * @param task The task to be created
     * @return [Result] wrapping the created [Task] if successful,
     * or an exception if the operation failed
     */
    override suspend fun createTask(task: Task): Result<Task> =
        remoteDataSource.createTask(taskMapper.mapToDto(task)).map { taskDto ->
            taskMapper.mapToDomain(taskDto)
        }

    /**
     * Updates an existing task.
     *
     * @param task The task to be updated with new data
     * @return [Result] wrapping the updated [Task] if successful,
     * or an exception if the operation failed
     */
    override suspend fun updateTask(task: Task): Result<Task> =
        remoteDataSource.updateTask(task.id, taskMapper.mapToDto(task)).map { taskDto ->
            taskMapper.mapToDomain(taskDto)
        }

    /**
     * Deletes a task by its ID.
     *
     * @param taskId The unique identifier of the task to delete
     * @return [Result] with success or failure
     */
    override suspend fun deleteTask(taskId: Long): Result<Unit> =
        remoteDataSource.deleteTask(taskId)
}