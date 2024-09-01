package edu.mike.frontend.taskapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.mike.frontend.taskapp.presentation.ui.screens.TaskDetailScreen
import edu.mike.frontend.taskapp.presentation.ui.screens.TaskListScreen
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel

/**
 * NavGraph is a composable function that sets up the navigation graph for the application.
 *
 * This function defines the navigation routes and the corresponding composable screens.
 *
 * @param navController The NavHostController used for navigation between screens.
 * @param taskViewModel The ViewModel instance used to observe the task data.
 * @param modifier The Modifier to be applied to the NavHost.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.TaskList.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.TaskList.route) {
            TaskListScreen(navController, taskViewModel)
        }
        composable(BottomNavItem.TaskDetail.route + "/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
            taskId?.let {
                TaskDetailScreen(
                    taskId = it,
                    taskViewModel = taskViewModel,
                    navController = navController
                )
            } ?: run {
                // Handle the case where taskId is null or not valid
                // For instance, navigate back or show an error message
            }
        }
        composable(BottomNavItem.Settings.route) {
            // SettingsScreen()
        }
    }
}