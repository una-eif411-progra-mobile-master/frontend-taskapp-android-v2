package edu.mike.frontend.taskapp.data.datasource

import edu.mike.frontend.taskapp.domain.model.Priority
import edu.mike.frontend.taskapp.domain.model.Status
import edu.mike.frontend.taskapp.domain.model.Task
import java.util.Date

/**
 * This class simulates the interaction with data.
 */
class TaskProvider {
    companion object {
        private val taskList = listOf(
            Task(
                id = 1,
                title = "Different Notes",
                notes = "Evaluate Students",
                createdDate = Date(),
                dueDate = Date(),
                priority = Priority(1, "High"),
                status = Status(1, "Pending")
            ),
            Task(
                id = 2,
                title = "More Notes",
                notes = "Coordinate with professors",
                createdDate = Date(),
                dueDate = Date(),
                priority = Priority(1, "High"),
                status = Status(1, "Pending")
            ),
            Task(
                id = 3,
                title = "Other Notes 3",
                notes = "We can not create ViewModel on our own. We need the ViewModelProviders utility provided by Android to create ViewModels.",
                createdDate = Date(),
                dueDate = Date(),
                priority = Priority(1, "High"),
                status = Status(1, "Pending")
            ),
            Task(
                id = 4,
                title = "Other Notes 4",
                notes = "In the UI part, We need to create an instance of",
                createdDate = Date(),
                dueDate = Date(),
                priority = Priority(1, "High"),
                status = Status(1, "Pending")
            ),
            Task(
                id = 5,
                title = "Other Notes 5",
                notes = "Create recyclerview in our main XML file.",
                createdDate = Date(),
                dueDate = Date(),
                priority = Priority(1, "High"),
                status = Status(1, "Pending")
            ),
            Task(
                id = 6,
                title = "Other Notes 6",
                notes = "We need to create an instance of the ViewModel",
                createdDate = Date(),
                dueDate = Date(),
                priority = Priority(1, "High"),
                status = Status(1, "Pending")
            ),
            Task(
                id = 7,
                title = "Other Notes 7",
                notes = "Also, create an adapter for the recyclerview",
                createdDate = Date(),
                dueDate = Date(),
                priority = Priority(1, "High"),
                status = Status(1, "Pending")
            ),
            Task(
                id = 8,
                title = "Other Notes 8",
                notes = "Retrofit is a “Type-safe HTTP client for Android and Java”.",
                createdDate = Date(),
                dueDate = Date(),
                priority = Priority(1, "High"),
                status = Status(1, "Pending")
            ),
            Task(
                id = 9,
                title = "Other Notes 9",
                notes = "Both are part of the same class. RetrofitService.kt",
                createdDate = Date(),
                dueDate = Date(),
                priority = Priority(1, "High"),
                status = Status(1, "Pending")
            ),
            Task(
                id = 10,
                title = "Other Notes 10",
                notes = "Create the Retrofit service instance using the retrofit.",
                createdDate = Date(),
                dueDate = Date(),
                priority = Priority(1, "High"),
                status = Status(1, "Pending")
            )
        )

        /**
         * Finds a task by its ID.
         * @param id The ID of the task to find.
         * @return The task with the given ID, or null if not found.
         */
        fun findTaskById(id: Long): Task? {
            return taskList.find { it.id == id }
        }

        /**
         * Finds all tasks.
         * @return A list of all tasks.
         */
        fun findAllTasks(): List<Task> {
            return taskList
        }
    }
}