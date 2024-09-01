package edu.mike.frontend.taskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import edu.mike.frontend.taskapp.navigation.NavGraph
import edu.mike.frontend.taskapp.presentation.ui.components.BottomNavigationBar
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel

/**
 * MainActivity is the entry point of the application.
 * It sets up the content view using Jetpack Compose and initializes the navigation graph.
 */
class MainActivity : ComponentActivity() {

    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskViewModel.findAllTask()

        setContent {
            val navController = rememberNavController()

            /**
             * Scaffold is a composable function that provides a basic structure for the screen.
             * It includes slots for a top bar, bottom bar, floating action button, and a content area.
             *
             * In this case, the Scaffold is used to include a bottom navigation bar and a content area
             * where the navigation graph is displayed.
             *
             * @param bottomBar The bottom navigation bar composable.
             * @param content The content area where the navigation graph is displayed.
             */
            Scaffold(
                bottomBar = { BottomNavigationBar(navController, taskViewModel) }
            ) { innerPadding ->
                NavGraph(
                    navController = navController,
                    taskViewModel = taskViewModel,
                    modifier = Modifier.padding(innerPadding)  // Pass the innerPadding as the modifier
                )
            }
        }
    }
}