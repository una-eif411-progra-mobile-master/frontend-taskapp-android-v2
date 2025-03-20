package edu.mike.frontend.taskapp.data.mapper

import edu.mike.frontend.taskapp.data.datasource.model.TaskDto
import edu.mike.frontend.taskapp.domain.model.Task

/**
 * Mapper class for converting between Task domain entities and TaskDto data objects
 */
class TaskMapper(
    private val priorityMapper: PriorityMapper,
    private val statusMapper: StatusMapper
) {
    /**
     * Maps a TaskDto to a domain Task entity
     * @param dto The data layer task object to convert
     * @return Domain Task object
     */
    fun mapToDomain(dto: TaskDto): Task = Task(
        id = dto.id,
        title = dto.title,
        notes = dto.notes,
        createdDate = dto.createdDate,
        dueDate = dto.dueDate,
        priority = priorityMapper.mapToDomain(dto.priority),
        status = statusMapper.mapToDomain(dto.status)
    )

    /**
     * Maps a domain Task to a TaskDto
     * @param domain The domain layer task object to convert
     * @return TaskDto object for data layer
     */
    fun mapToDto(domain: Task): TaskDto = TaskDto(
        id = domain.id,
        title = domain.title,
        notes = domain.notes,
        createdDate = domain.createdDate,
        dueDate = domain.dueDate,
        priority = priorityMapper.mapToDto(domain.priority),
        status = statusMapper.mapToDto(domain.status)
    )
}