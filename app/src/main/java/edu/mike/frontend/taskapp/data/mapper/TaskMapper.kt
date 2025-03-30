package edu.mike.frontend.taskapp.data.mapper

import edu.mike.frontend.taskapp.data.model.PriorityDto
import edu.mike.frontend.taskapp.data.model.StatusDto
import edu.mike.frontend.taskapp.data.model.TaskDto
import edu.mike.frontend.taskapp.domain.model.Priority
import edu.mike.frontend.taskapp.domain.model.Status
import edu.mike.frontend.taskapp.domain.model.Task
import javax.inject.Inject

class TaskMapper @Inject constructor() {
    fun mapToDomain(taskDto: TaskDto): Task {
        return Task(
            id = taskDto.id,
            title = taskDto.title,
            notes = taskDto.notes,
            createdDate = taskDto.createdDate,
            dueDate = taskDto.dueDate,
            priority = mapToDomainPriority(taskDto.priority),
            status = mapToDomainStatus(taskDto.status)
        )
    }

    fun mapToDomainList(tasksDto: List<TaskDto>): List<Task> {
        return tasksDto.map { mapToDomain(it) }
    }

    fun mapToDto(task: Task): TaskDto {
        return TaskDto(
            id = task.id,
            title = task.title,
            notes = task.notes,
            createdDate = task.createdDate,
            dueDate = task.dueDate,
            priority = mapToDtoPriority(task.priority),
            status = mapToDtoStatus(task.status)
        )
    }

    private fun mapToDomainPriority(priorityDto: PriorityDto): Priority {
        return Priority(
            id = priorityDto.id,
            label = priorityDto.label
        )
    }

    private fun mapToDomainStatus(statusDto: StatusDto): Status {
        return Status(
            id = statusDto.id,
            label = statusDto.label
        )
    }

    private fun mapToDtoPriority(priority: Priority): PriorityDto {
        return PriorityDto(
            id = priority.id,
            label = priority.label
        )
    }

    private fun mapToDtoStatus(status: Status): StatusDto {
        return StatusDto(
            id = status.id,
            label = status.label
        )
    }
}