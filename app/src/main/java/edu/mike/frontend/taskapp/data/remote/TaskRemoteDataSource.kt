package edu.mike.frontend.taskapp.data.remote

import edu.mike.frontend.taskapp.data.di.TaskService
import edu.mike.frontend.taskapp.data.model.TaskDto
import javax.inject.Inject

class TaskRemoteDataSource @Inject constructor(
    private val taskService: TaskService
) {
    suspend fun getAllTasks(): Result<List<TaskDto>> {
        return try {
            val response = taskService.getAllTasks()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error fetching tasks: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTaskById(id: Long): Result<TaskDto> {
        return try {
            val response = taskService.getTaskById(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Task not found"))
            } else {
                Result.failure(Exception("Error fetching task: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}