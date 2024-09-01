package edu.mike.frontend.taskapp.data.model

import java.util.*

/**
 * This class represents a Task
 */
data class Task(
    var id: Long,
    var title: String,
    var notes: String,
    var createDate: Date,
    var dueDate: Date,
    var priority: Priority,
    var status: Status,
)