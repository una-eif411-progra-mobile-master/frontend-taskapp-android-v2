package edu.mike.frontend.taskapp.data.network

import edu.mike.frontend.taskapp.data.model.Task
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit service interface for Task-related API endpoints.
 */
interface TaskService {

    /**
     * Fetches all tasks from the API.
     *
     * @return A Response object containing a list of Task objects.
     */
    @GET("tasks")
    suspend fun getAllTasks(): Response<List<Task>>

    /**
     * Fetches a task by its ID from the API.
     *
     * @param id The ID of the task to fetch.
     * @return A Response object containing the Task object.
     */
    @GET("tasks/{id}")
    suspend fun getTaskById(@Path("id") id: Int): Response<Task>
}