package edu.mike.frontend.taskapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import edu.mike.frontend.taskapp.presentation.ui.screens.SettingsScreen
import edu.mike.frontend.taskapp.presentation.ui.screens.TaskDetailScreen
import edu.mike.frontend.taskapp.presentation.ui.screens.TaskListScreen
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel

/**
 * NavGraph is a composable function that sets up the navigation graph for the application.
 * It defines all available navigation routes and associates them with their respective screens.
 *
 * @param navController The NavHostController used for navigation between screens
 * @param taskViewModel The ViewModel instance used to manage task data across screens
 * @param paddingValues The padding values to apply to the screens, typically from Scaffold
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    paddingValues: PaddingValues = PaddingValues()
) {
    NavHost(navController = navController, startDestination = BottomNavItem.Routes.TASK_LIST) {
        // Task list screen - main entry point of the application
        composable(
            route = BottomNavItem.Routes.TASK_LIST,
            arguments = emptyList()
        ) {
            TaskListScreen(
                navController = navController,
                taskViewModel = taskViewModel,
                paddingValues = paddingValues
            )
        }

        // Task detail screen - displays details for a specific task
        composable(
            route = "${BottomNavItem.Routes.TASK_DETAIL}/{taskId}",
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.LongType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId") ?: -1L
            TaskDetailScreen(
                taskId = taskId,
                taskViewModel = taskViewModel,
                navController = navController
            )
        }

        // Settings screen - handles application configuration
        composable(
            route = BottomNavItem.Routes.SETTINGS,
            arguments = emptyList()
        ) {
            SettingsScreen(
                navController = navController,
                paddingValues = paddingValues
            )
        }
    }
}