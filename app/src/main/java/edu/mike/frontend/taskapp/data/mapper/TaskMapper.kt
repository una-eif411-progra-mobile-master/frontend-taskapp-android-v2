package edu.mike.frontend.taskapp.data.mapper

import edu.mike.frontend.taskapp.data.remote.dto.TaskDto
import edu.mike.frontend.taskapp.domain.model.Task
import javax.inject.Inject

/**
 * Mapper class for converting between Task domain entities and TaskDto data objects.
 * Delegates mapping of nested objects to specialized mappers for better separation of concerns.
 *
 * @property priorityMapper Mapper for converting between Priority and PriorityDto
 * @property statusMapper Mapper for converting between Status and StatusDto
 */
class TaskMapper @Inject constructor(
    private val priorityMapper: PriorityMapper,
    private val statusMapper: StatusMapper
) {
    /**
     * Maps a TaskDto to a domain Task entity
     *
     * @param taskDto The data layer task object to convert
     * @return Domain Task object
     */
    fun mapToDomain(taskDto: TaskDto): Task {
        return Task(
            id = taskDto.id,
            title = taskDto.title,
            notes = taskDto.notes,
            createdDate = taskDto.createdDate,
            dueDate = taskDto.dueDate,
            priority = priorityMapper.mapToDomain(taskDto.priority),
            status = statusMapper.mapToDomain(taskDto.status)
        )
    }

    /**
     * Maps a list of TaskDto objects to domain Task entities
     *
     * @param tasksDto List of data layer task objects to convert
     * @return List of domain Task objects
     */
    fun mapToDomainList(tasksDto: List<TaskDto>): List<Task> {
        return tasksDto.map { mapToDomain(it) }
    }

    /**
     * Maps a domain Task to a TaskDto
     *
     * @param task The domain layer task object to convert
     * @return TaskDto object for data layer
     */
    fun mapToDto(task: Task): TaskDto {
        return TaskDto(
            id = task.id,
            title = task.title,
            notes = task.notes,
            createdDate = task.createdDate,
            dueDate = task.dueDate,
            priority = priorityMapper.mapToDto(task.priority),
            status = statusMapper.mapToDto(task.status)
        )
    }

    /**
     * Maps a list of domain Task objects to TaskDto entities
     *
     * @param tasks List of domain task objects to convert
     * @return List of TaskDto objects
     */
    fun mapToDtoList(tasks: List<Task>): List<TaskDto> {
        return tasks.map { mapToDto(it) }
    }
}