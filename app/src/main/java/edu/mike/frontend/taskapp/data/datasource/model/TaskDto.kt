package edu.mike.frontend.taskapp.data.datasource.model

import java.util.Date

/**
 * This class represents the Dto of a Task.
 * @property id The unique identifier of the task.
 * @property title The title of the task.
 * @property notes The notes associated with the task.
 * @property createdDate The date the task was created.
 * @property dueDate The date the task is due.
 * @property priority The priority of the task.
 * @property status The status of the task.
 */
data class TaskDto(
    val id: Long,
    val title: String,
    val notes: String,
    val createdDate: Date,
    val dueDate: Date,
    val priority: PriorityDto,
    val status: StatusDto
)