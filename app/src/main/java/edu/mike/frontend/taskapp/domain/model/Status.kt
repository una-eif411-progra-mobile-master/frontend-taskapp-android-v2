package edu.mike.frontend.taskapp.domain.model

/**
 * This class represents the Status of a Task.
 * @property id The unique identifier of the status.
 * @property label The label of the status.
 */
data class Status(
    val id: Long,
    val label: String
)