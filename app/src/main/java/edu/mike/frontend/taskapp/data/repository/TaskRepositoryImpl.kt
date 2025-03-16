package edu.mike.frontend.taskapp.data.repository

import edu.mike.frontend.taskapp.data.datasource.TaskDataSource
import edu.mike.frontend.taskapp.data.mapper.TaskMapper
import edu.mike.frontend.taskapp.domain.model.Task
import edu.mike.frontend.taskapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * TaskRepository implementation
 * @param dataSource The data source for task data
 * @param taskMapper The mapper for converting between domain and data layer task objects
 */
class TaskRepositoryImpl(
    private val dataSource: TaskDataSource,
    private val taskMapper: TaskMapper
) : TaskRepository {

    override suspend fun getTasks(): Flow<List<Task>> =
        dataSource.getTasks().map { dtos ->
            dtos.map { taskMapper.mapToDomain(it) }
        }

    override suspend fun getTaskById(id: Long): Task? =
        dataSource.getTaskById(id)?.let { taskMapper.mapToDomain(it) }

    override suspend fun insertTask(task: Task) {
        dataSource.insertTask(taskMapper.mapToDto(task))
    }

    override suspend fun updateTask(task: Task) {
        dataSource.updateTask(taskMapper.mapToDto(task))
    }

    override suspend fun deleteTask(task: Task) {
        dataSource.deleteTask(taskMapper.mapToDto(task))
    }
}