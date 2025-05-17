package edu.mike.frontend.taskapp.data.remote.api

import edu.mike.frontend.taskapp.data.remote.dto.TaskDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Retrofit service interface that defines the API endpoints for task operations.
 * This service interacts with the remote task API using HTTP methods.
 */
interface TaskService {
    /**
     * Retrieves all tasks from the remote API.
     *
     * @return [Response] containing a list of [TaskDto] objects if successful
     */
    @GET("tasks")
    suspend fun getAllTasks(): Response<List<TaskDto>>

    /**
     * Retrieves a specific task by its unique identifier.
     *
     * @param id The unique identifier of the task to retrieve
     * @return [Response] containing the requested [TaskDto] if successful
     */
    @GET("tasks/{id}")
    suspend fun getTaskById(@Path("id") id: Long): Response<TaskDto>

    /**
     * Creates a new task in the remote API.
     *
     * @param task The [TaskDto] object containing the task data to create
     * @return [Response] containing the created [TaskDto] with server-assigned ID if successful
     */
    @POST("tasks")
    suspend fun createTask(@Body task: TaskDto): Response<TaskDto>

    /**
     * Updates an existing task in the remote API.
     *
     * @param id The unique identifier of the task to update
     * @param task The [TaskDto] object containing the updated task data
     * @return [Response] containing the updated [TaskDto] if successful
     */
    @PUT("tasks/{id}")
    suspend fun updateTask(
        @Path("id") id: Long,
        @Body task: TaskDto
    ): Response<TaskDto>

    /**
     * Deletes a task from the remote API.
     *
     * @param id The unique identifier of the task to delete
     * @return [Response] indicating the success of the operation
     */
    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: Long): Response<Unit>
}