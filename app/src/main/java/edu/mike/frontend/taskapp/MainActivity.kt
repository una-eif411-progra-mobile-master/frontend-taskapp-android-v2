package edu.mike.frontend.taskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel
import edu.mike.frontend.taskapp.navigation.NavGraph

/**
 * MainActivity is the entry point of the application.
 * It sets up the content view using Jetpack Compose and initializes the navigation graph.
 */
class MainActivity : ComponentActivity() {

    // ViewModel instance for managing the task list and task data
    private val taskViewModel: TaskViewModel by viewModels()

    /**
     * onCreate is called when the activity is first created.
     * We are setting the content view to a Composable function using setContent.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ensure the task list is loaded when the activity starts
        taskViewModel.findAllTask()

        // Use Jetpack Compose to set the layout of the activity
        setContent {
            // Initialize the NavController
            val navController = rememberNavController()

            // Set up the navigation graph with the NavController and ViewModel
            NavGraph(navController = navController, taskViewModel = taskViewModel)
        }
    }
}