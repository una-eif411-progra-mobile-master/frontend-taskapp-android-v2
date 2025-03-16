package edu.mike.frontend.taskapp.presentation.navigation

/**
 * Contains all navigation route constants for the application.
 *
 * Using a sealed class with objects ensures type safety and prevents errors from misspelled route strings.
 */
sealed class NavRoutes {
    data object TaskList : NavRoutes() {
        const val ROUTE = "taskList"
    }

    data object TaskDetail : NavRoutes() {
        const val ROUTE = "taskDetail/{taskId}"
        const val ARG_TASK_ID = "taskId" // Changed to uppercase with underscores

        fun createRoute(taskId: Long) = "taskDetail/$taskId"
    }
}