package edu.mike.frontend.taskapp.data.model

import java.util.*

/**
 * This class represents a Task
 */
data class Task(
    val id: Int,
    val title: String,
    val notes: String,
    val createDate: Date,
    val dueDate: Date,
    val priority: Priority,  // Nested priority object
    val status: Status       // Nested status object
)