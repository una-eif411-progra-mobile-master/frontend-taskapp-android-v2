package edu.mike.frontend.taskapp.data.datasource

import edu.mike.frontend.taskapp.data.datasource.model.TaskDto
import kotlinx.coroutines.flow.Flow

/**
 * TaskDataSource interface
 */
interface TaskDataSource {
    suspend fun getTasks(): Flow<List<TaskDto>>
    suspend fun getTaskById(id: Long): TaskDto?
    suspend fun insertTask(taskDto: TaskDto)
    suspend fun updateTask(taskDto: TaskDto)
    suspend fun deleteTask(taskDto: TaskDto)
}