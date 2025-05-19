package edu.mike.frontend.taskapp.presentation.navigation

/**
 * Contains navigation route constants used throughout the app.
 * Each nested object represents a destination screen with its route and any required arguments.
 */
object NavRoutes {
    /**
     * Task list screen showing all available tasks
     */
    object TaskList {
        const val ROUTE = "taskList"
    }

    /**
     * Task detail screen showing detailed information for a specific task
     */
    object TaskDetail {
        const val ARG_TASK_ID = "taskId"
        const val ROUTE = "taskDetail/{$ARG_TASK_ID}"

        /**
         * Creates a route to a specific task with the given ID
         */
        fun createRoute(taskId: Long): String = "taskDetail/$taskId"
    }

    /**
     * Settings screen for application configuration
     */
    object Settings {
        const val ROUTE = "settings"
    }

    /**
     * Login screen for authentication
     */
    object Login {
        const val ROUTE = "login"
    }
}