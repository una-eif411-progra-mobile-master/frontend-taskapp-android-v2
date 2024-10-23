package edu.mike.frontend.taskapp.data.network

import edu.mike.frontend.taskapp.data.model.LoginRequest
import edu.mike.frontend.taskapp.data.model.LoginResponse
import edu.mike.frontend.taskapp.data.model.Task
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    /**
     * Logs in a user with the provided credentials.
     *
     * @param loginRequest The LoginRequest object containing the user's credentials.
     * @return A Response object containing the LoginResponse object.
     */
    @POST("users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}