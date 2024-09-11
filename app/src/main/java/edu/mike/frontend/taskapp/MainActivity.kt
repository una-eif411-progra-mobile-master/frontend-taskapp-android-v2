package edu.mike.frontend.taskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.mike.frontend.taskapp.navigation.NavGraph
import edu.mike.frontend.taskapp.presentation.ui.components.BottomNavigationBar
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

    // The TaskViewModel instance is obtained using the viewModels() delegate, which allows
    // this activity to share the same instance of the view model across configuration changes
    // such as screen rotations.
    private val taskViewModel: TaskViewModel by viewModels()

    /**
     * The onCreate method is called when the activity is first created. It sets up the UI and
     * initializes components such as the task list and navigation.
     *
     * In this method:
     *  - The [taskViewModel] fetches all tasks.
     *  - The [setContent] block sets the composable layout for the activity.
     *  - The [Scaffold] composable is used to provide a structured layout that includes a
     *    bottom navigation bar and content space.
     */
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
                // The BottomNavigationBar is provided as the bottom bar for the Scaffold. It allows
                // navigation between different sections of the app, such as Task List and Settings.
                bottomBar = { BottomNavigationBar(navController, taskViewModel) }
            ) { innerPadding ->
                // The content area is handled by the NavGraph composable, which defines
                // the navigation logic between screens (Task List, Task Details, etc.).
                // The `innerPadding` ensures the content is correctly padded, avoiding overlap
                // with the bottom navigation bar.
                NavGraph(
                    navController = navController,
                    taskViewModel = taskViewModel,
                    modifier = Modifier.padding(innerPadding)  // Pass the innerPadding as the modifier
                )
            }
        }
    }
}