package edu.mike.frontend.taskapp.presentation.navigation

/**
 * NavRoutes defines all possible destinations
 * Contains all navigation route constants for the application.
 *
 * Using a sealed class with objects ensures type safety and prevents errors from misspelled route strings.
 */
sealed class NavRoutes {
    data object TaskList : NavRoutes() {
        const val ROUTE = "taskList"
    }

    /**
     * Defines the route for the task detail screen.
     * This screen displays detailed information about a specific task.
     */
    data object TaskDetail : NavRoutes() {
        const val ROUTE = "taskDetail/{taskId}"
        const val ARG_TASK_ID = "taskId" // Changed to uppercase with underscores

        fun createRoute(taskId: Long) = "taskDetail/$taskId"
    }

    /**
     * Defines the route for the settings screen.
     * This screen displays application configuration options.
     */
    data object Settings : NavRoutes() {
        const val ROUTE = "settings"
    }
}