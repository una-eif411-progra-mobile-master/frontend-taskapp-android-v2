package edu.mike.frontend.taskapp.data.di

import edu.mike.frontend.taskapp.data.model.TaskDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TaskService {
    @GET("tasks")
    suspend fun getAllTasks(): Response<List<TaskDto>>

    @GET("tasks/{id}")
    suspend fun getTaskById(@Path("id") id: Long): Response<TaskDto>
}