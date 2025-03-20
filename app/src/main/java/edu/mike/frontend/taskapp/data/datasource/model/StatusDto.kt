package edu.mike.frontend.taskapp.data.datasource.model

/**
 * This class represents the Dto of a Status.
 * @property id The unique identifier of the status.
 * @property label The label of the status.
 */
data class StatusDto(
    val id: Long,
    val label: String
)