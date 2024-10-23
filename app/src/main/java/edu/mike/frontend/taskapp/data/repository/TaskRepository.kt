package edu.mike.frontend.taskapp.data.repository

import edu.mike.frontend.taskapp.data.model.LoginRequest
import edu.mike.frontend.taskapp.data.model.LoginResponse
import edu.mike.frontend.taskapp.data.model.Task
import edu.mike.frontend.taskapp.data.network.TaskService
import retrofit2.Response
import javax.inject.Inject

/**
 * Repository class for managing task data operations.
 *
 * @property taskService The TaskService instance for network operations.
 */
class TaskRepository @Inject constructor(
    private val taskService: TaskService
) {

    /**
     * Fetches all tasks from the API and returns them as a list.
     *
     * @return A list of Task objects or null if an error occurs.
     */
    suspend fun getAllTasks(): List<Task>? {
        return try {
            val response = taskService.getAllTasks()
            if (response.isSuccessful) {
                // If response is successful but the body is null, handle that case
                response.body() ?: emptyList()
            } else {
                // Log or handle error response case
                null
            }
        } catch (e: Exception) {
            // Catching any exceptions that may occur during the network call
            e.printStackTrace()
            null
        }
    }

    /**
     * Fetches a task by its ID from the API.
     *
     * @param id The ID of the task to fetch.
     * @return The Task object or null if an error occurs.
     */
    suspend fun getTaskById(id: Int): Task? {
        return try {
            val response = taskService.getTaskById(id)
            if (response.isSuccessful) {
                response.body() // Handle null body here
            } else {
                // Log or handle error response case
                null
            }
        } catch (e: Exception) {
            // Catching any exceptions that may occur during the network call
            e.printStackTrace()
            null
        }
    }

    /**
     * Logs in a user with the provided credentials.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The LoginResponse object or null if an error occurs.
     */
    suspend fun login(username: String, password: String): LoginResponse? {
        val response: Response<LoginResponse> = taskService.login(LoginRequest(username, password))
        return if (response.isSuccessful) response.body() else null
    }
}