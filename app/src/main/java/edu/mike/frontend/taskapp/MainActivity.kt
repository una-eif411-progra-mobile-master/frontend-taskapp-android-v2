package edu.mike.frontend.taskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import edu.mike.frontend.taskapp.navigation.NavGraph
import edu.mike.frontend.taskapp.presentation.ui.theme.TaskAppTheme
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel

/**
 * Main entry point of the Task Application.
 *
 * This activity serves as the primary UI container for the Task Application,
 * demonstrating:
 * - Jetpack Compose for declarative UI development
 * - MVVM architecture for state management
 * - Jetpack Navigation for screen transitions
 * - Material3 design principles
 */
class MainActivity : ComponentActivity() {
    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ensure the task list is loaded when the activity starts
        taskViewModel.findAllTasks()

        setContent {
            TaskAppTheme {
                val navController = rememberNavController()
                TaskAppScreen(navController, taskViewModel)
            }
        }
    }
}

/**
 * Main screen that integrates the navigation system.
 *
 * @param navController Controller for managing navigation between screens
 * @param taskViewModel ViewModel instance for managing tasks
 */
@Composable
fun TaskAppScreen(navController: NavHostController, taskViewModel: TaskViewModel) {
    val task by taskViewModel.task.collectAsState()
    val taskList by taskViewModel.taskList.collectAsState()

    Scaffold { paddingValues ->
        NavGraph(
            navController = navController,
            taskViewModel = taskViewModel,
            paddingValues = paddingValues
        )
    }
}