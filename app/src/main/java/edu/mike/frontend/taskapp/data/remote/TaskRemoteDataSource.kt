package edu.mike.frontend.taskapp.data.remote

import edu.mike.frontend.taskapp.data.remote.api.TaskService
import edu.mike.frontend.taskapp.data.remote.dto.TaskDto
import retrofit2.Response
import javax.inject.Inject

/**
 * Remote data source for task operations.
 * Handles all network operations related to tasks using [TaskService].
 *
 * @property taskService The service interface for task-related API calls
 */
class TaskRemoteDataSource @Inject constructor(
    private val taskService: TaskService
) {
    /**
     * Retrieves all tasks from the remote API.
     *
     * @return [Result] containing a list of [TaskDto] if successful,
     * or an exception if the operation failed
     */
    suspend fun getAllTasks(): Result<List<TaskDto>> = safeApiCall {
        taskService.getAllTasks()
    }

    /**
     * Retrieves a specific task by its ID.
     *
     * @param id The unique identifier of the task
     * @return [Result] containing the [TaskDto] if successful,
     * or an exception if the operation failed
     */
    suspend fun getTaskById(id: Long): Result<TaskDto> = safeApiCall {
        taskService.getTaskById(id)
    }

    /**
     * Creates a new task.
     *
     * @param taskDto The task data to create
     * @return [Result] containing the created [TaskDto] if successful,
     * or an exception if the operation failed
     */
    suspend fun createTask(taskDto: TaskDto): Result<TaskDto> = safeApiCall {
        taskService.createTask(taskDto)
    }

    /**
     * Updates an existing task.
     *
     * @param id The unique identifier of the task to update
     * @param taskDto The updated task data
     * @return [Result] containing the updated [TaskDto] if successful,
     * or an exception if the operation failed
     */
    suspend fun updateTask(id: Long, taskDto: TaskDto): Result<TaskDto> = safeApiCall {
        taskService.updateTask(id, taskDto)
    }

    /**
     * Deletes a task by its ID.
     *
     * @param id The unique identifier of the task to delete
     * @return [Result] with success or failure
     */
    suspend fun deleteTask(id: Long): Result<Unit> = safeApiCall {
        taskService.deleteTask(id)
    }

    /**
     * Helper function to handle API calls safely.
     *
     * @param apiCall The suspending function making the API call
     * @return [Result] containing the data if successful, or an exception if failed
     */
    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> = try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Response body was null"))
        } else {
            val errorBody = response.errorBody()?.string()
            Result.failure(Exception("API error ${response.code()}: $errorBody"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}