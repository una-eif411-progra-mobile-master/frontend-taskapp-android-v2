package edu.mike.frontend.taskapp.data.datasource

import edu.mike.frontend.taskapp.data.datasource.model.TaskDto
import edu.mike.frontend.taskapp.data.mapper.TaskMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * TaskDataSource implementation
 * @param taskMapper The mapper for converting between data and domain layer task objects
 */
class TaskDataSourceImpl(
    private val taskMapper: TaskMapper
) : TaskDataSource {

    override suspend fun getTasks(): Flow<List<TaskDto>> = flow {
        val tasks = TaskProvider.findAllTasks()
        emit(tasks.map { taskMapper.mapToDto(it) })
    }

    override suspend fun getTaskById(id: Long): TaskDto? =
        TaskProvider.findTaskById(id)?.let { taskMapper.mapToDto(it) }

    override suspend fun insertTask(taskDto: TaskDto) {
        // Mock implementation - no persistence needed
    }

    override suspend fun updateTask(taskDto: TaskDto) {
        // Mock implementation - no persistence needed
    }

    override suspend fun deleteTask(taskDto: TaskDto) {
        // Mock implementation - no persistence needed
    }
}