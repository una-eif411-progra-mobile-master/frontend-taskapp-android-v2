package edu.mike.frontend.taskapp.navigation

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
 * @param navController The NavHostController used for navigation between screens.
 * @param taskViewModel The ViewModel instance used to observe the task data.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    paddingValues: PaddingValues
) {
    NavHost(navController = navController, startDestination = "taskList") {
        composable("taskList") {
            TaskListScreen(navController, taskViewModel, paddingValues)
        }
        composable(
            "taskDetail/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.LongType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId") ?: 0L
            TaskDetailScreen(
                taskId = taskId,
                taskViewModel = taskViewModel,
                navController = navController,
                paddingValues = paddingValues
            )
        }
    }
}