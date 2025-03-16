package edu.mike.frontend.taskapp.data.datasource.model

/**
 * This class represents the Dto of a Priority.
 * @property id The unique identifier of the priority.
 * @property label The label of the priority.
 */
data class PriorityDto(
    val id: Long,
    val label: String
)