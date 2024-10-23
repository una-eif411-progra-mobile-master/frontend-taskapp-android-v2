package edu.mike.frontend.taskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.mike.frontend.taskapp.navigation.NavGraph
import edu.mike.frontend.taskapp.presentation.ui.components.BottomNavigationBar
import edu.mike.frontend.taskapp.presentation.viewmodel.LoginViewModel
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel

/**
 * MainActivity is the entry point of the application.
 *
 * This activity is responsible for setting up the content view using Jetpack Compose and
 * initializing the necessary components for the user interface, such as the navigation graph,
 * view model, and bottom navigation.
 *
 * The MainActivity is annotated with [ComponentActivity] to support Jetpack Compose and
 * use lifecycle-aware components.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // ViewModel instances for login and task management
    private val loginViewModel: LoginViewModel by viewModels()
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Fetch all tasks when the activity is created.
        taskViewModel.findAllTasks()

        setContent {
            // RememberNavController is used to handle navigation between composable screens.
            val navController = rememberNavController()

            /**
             * Scaffold is a composable function that provides the basic structure of the app's UI.
             * It includes the following slots:
             * - `bottomBar`: A bottom navigation bar that allows users to switch between screens.
             * - `content`: The area where the main screen content is shown, managed by the
             *   [NavGraph] for navigation between different screens.
             */
            Scaffold(
                bottomBar = {
                    // Get the current back stack entry to determine the current route
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    // Show bottom navigation bar only if the current screen is not "login"
                    if (currentRoute != "login") {
                        BottomNavigationBar(navController, taskViewModel)
                    }
                }
            ) { innerPadding ->
                // Set up the navigation graph with the collected padding
                NavGraph(
                    navController = navController,
                    loginViewModel = loginViewModel,
                    taskViewModel = taskViewModel,
                    modifier = Modifier.padding(innerPadding)  // Pass the innerPadding as the modifier
                )
            }
        }
    }
}