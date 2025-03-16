package edu.mike.frontend.taskapp.domain.repository

import edu.mike.frontend.taskapp.domain.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * This interface represents the TaskRepository.
 */
interface TaskRepository {
    suspend fun getTasks(): Flow<List<Task>>
    suspend fun getTaskById(id: Long): Task?
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
}