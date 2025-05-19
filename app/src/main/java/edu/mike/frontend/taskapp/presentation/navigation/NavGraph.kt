package edu.mike.frontend.taskapp.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import edu.mike.frontend.taskapp.presentation.ui.screens.LoginScreen
import edu.mike.frontend.taskapp.presentation.ui.screens.SettingsScreen
import edu.mike.frontend.taskapp.presentation.ui.screens.TaskDetailScreen
import edu.mike.frontend.taskapp.presentation.ui.screens.TaskListScreen
import edu.mike.frontend.taskapp.presentation.viewmodel.LoginViewModel
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel

/**
 * Sets up the navigation graph for the application.
 *
 * This composable function configures the navigation structure between different screens in the app.
 * It handles both authenticated and unauthenticated states, displaying either the login flow
 * or the main app screens depending on the authentication status.
 *
 * Routes:
 * - "login": Authentication screen for users to sign in
 * - "taskList": Shows a list of all tasks (requires authentication)
 * - "taskDetail/{taskId}": Shows details of a specific task (requires authentication)
 * - "settings": Shows app settings (requires authentication)
 *
 * @param navController The NavHostController used for navigation between screens.
 * @param taskViewModel The ViewModel instance used to observe and manipulate task data across screens.
 * @param loginViewModel The ViewModel instance for authentication operations.
 * @param paddingValues Provides padding values to account for UI elements like the app bar or bottom navigation.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
    loginViewModel: LoginViewModel,
    paddingValues: PaddingValues
) {
    // Determine start destination based on login status
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()

    val startDestination = if (isLoggedIn) {
        NavRoutes.TaskList.ROUTE
    } else {
        NavRoutes.Login.ROUTE
    }

    NavHost(navController = navController, startDestination = startDestination) {
        // Login screen (unauthenticated)
        composable(NavRoutes.Login.ROUTE) {
            LoginScreen(
                loginViewModel = loginViewModel,
                onLoginSuccess = {
                    navController.navigate(NavRoutes.TaskList.ROUTE) {
                        // Clear back stack so user can't navigate back to login screen
                        popUpTo(NavRoutes.Login.ROUTE) { inclusive = true }
                    }
                }
            )
        }

        // TaskList screen (authenticated)
        composable(NavRoutes.TaskList.ROUTE) {
            TaskListScreen(navController, taskViewModel, paddingValues)
        }

        // TaskDetail screen (authenticated)
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

        // Settings screen (authenticated)
        composable(NavRoutes.Settings.ROUTE) {
            SettingsScreen(
                navController = navController,
                paddingValues = paddingValues,
                onLogout = {
                    loginViewModel.logout()
                    navController.navigate(NavRoutes.Login.ROUTE) {
                        // Clear back stack to prevent navigation back to authenticated screens
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}