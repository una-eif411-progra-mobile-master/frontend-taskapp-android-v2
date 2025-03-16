package edu.mike.frontend.taskapp.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import edu.mike.frontend.taskapp.presentation.ui.screens.TaskDetailScreen
import edu.mike.frontend.taskapp.presentation.ui.screens.TaskListScreen
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel

/**
 * Sets up the navigation graph for the application.
 *
 * This composable function configures the navigation structure between different screens in the app.
 * It defines two main routes:
 * - "taskList": Shows a list of all tasks
 * - "taskDetail/{taskId}": Shows details of a specific task based on the taskId
 *
 * @param navController The NavHostController used for navigation between screens.
 *                     Controls the navigation flow and back stack behavior.
 * @param taskViewModel The ViewModel instance used to observe and manipulate task data across screens.
 * @param paddingValues Provides padding values to account for UI elements like the app bar or bottom navigation,
 *                     ensuring content doesn't get hidden behind system UI elements.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    paddingValues: PaddingValues
) {
    NavHost(navController = navController, startDestination = NavRoutes.TaskList.ROUTE) {
        // TaskList screen - Entry point of the application
        // Displays all available tasks in a scrollable list with options to select individual tasks
        composable(NavRoutes.TaskList.ROUTE) {
            TaskListScreen(navController, taskViewModel, paddingValues)
        }

        // TaskDetail screen - Shows detailed information about a specific task
        // Retrieves the task based on the taskId parameter from the navigation route
        // Handles potential error cases with a default value of 0L
        composable(
            route = NavRoutes.TaskDetail.ROUTE,
            arguments = listOf(navArgument(NavRoutes.TaskDetail.ARG_TASK_ID) {
                type = NavType.LongType
            })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong(NavRoutes.TaskDetail.ARG_TASK_ID) ?: 0L
            TaskDetailScreen(
                taskId = taskId,
                taskViewModel = taskViewModel,
                navController = navController,
                paddingValues = paddingValues
            )
        }
    }
}