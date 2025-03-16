package edu.mike.frontend.taskapp.domain.model

/**
 * This class represents the Priority of a Task.
 * @property id The unique identifier of the priority.
 * @property label The label of the priority.
 */
data class Priority(
    val id: Long,
    val label: String
)