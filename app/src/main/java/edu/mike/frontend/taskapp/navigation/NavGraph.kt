package edu.mike.frontend.taskapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.mike.frontend.taskapp.presentation.ui.screens.LoginScreen
import edu.mike.frontend.taskapp.presentation.ui.screens.TaskDetailScreen
import edu.mike.frontend.taskapp.presentation.ui.screens.TaskListScreen
import edu.mike.frontend.taskapp.presentation.viewmodel.LoginViewModel
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel

/**
 * NavGraph is a composable function that sets up the navigation graph for the application.
 *
 * This function defines the navigation routes and the corresponding composable screens.
 *
 * @param navController The NavHostController used for navigation between screens.
 * @param loginViewModel The ViewModel instance used to observe the login state.
 * @param taskViewModel The ViewModel instance used to observe the task data.
 * @param modifier The Modifier to be applied to the NavHost.
 */
@Composable
fun NavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    taskViewModel: TaskViewModel,
    modifier: Modifier = Modifier
) {
    // Collect the login state from the ViewModel
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()

    // Set up the navigation graph
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) BottomNavItem.TaskList.route else "login",
        modifier = modifier
    ) {
        // Define the composable for the login screen
        composable("login") {
            LoginScreen(
                loginViewModel = loginViewModel,
                onLoginSuccess = {
                    // Navigate to the task list screen on successful login
                    navController.navigate(BottomNavItem.TaskList.route) {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        // Define the composable for the task list screen
        composable(BottomNavItem.TaskList.route) {
            TaskListScreen(navController, taskViewModel)
        }
        // Define the composable for the task detail screen
        composable(BottomNavItem.TaskDetail.route + "/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toIntOrNull()
            taskId?.let {
                TaskDetailScreen(
                    taskId = it,
                    taskViewModel = taskViewModel,
                    navController = navController
                )
            }
        }
        // Placeholder for the settings screen
        composable(BottomNavItem.Settings.route) {
            // SettingsScreen()
        }
    }
}