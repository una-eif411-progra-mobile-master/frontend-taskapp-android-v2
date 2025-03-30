package edu.mike.frontend.taskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.mike.frontend.taskapp.presentation.navigation.NavGraph
import edu.mike.frontend.taskapp.presentation.ui.components.BottomNavigationBar
import edu.mike.frontend.taskapp.presentation.ui.layout.MainLayout
import edu.mike.frontend.taskapp.presentation.ui.theme.TaskAppTheme
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel

/**
 * Main activity that serves as the entry point for the application.
 * Initializes the TaskViewModel and sets up the Compose UI with the main screen.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    /**
     * ViewModel instance that manages task-related data and business logic.
     * Injected by Hilt.
     */
    private val taskViewModel: TaskViewModel by viewModels()

    /**
     * Initializes the activity and sets up the Compose UI.
     * Applies the app theme and renders the main screen.
     *
     * @param savedInstanceState If non-null, this activity is being re-constructed from a
     * previous saved state as given here.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TaskAppTheme {
                MainScreen(taskViewModel)
            }
        }
    }
}

/**
 * Main screen composable that serves as the container for the application UI.
 * It handles loading states, initializes the navigation controller,
 * and sets up the app's main layout structure.
 *
 * @param taskViewModel The ViewModel that provides access to task data and business logic
 */
@Composable
fun MainScreen(taskViewModel: TaskViewModel) {
    val navController = rememberNavController()
    val taskListState by taskViewModel.taskList.collectAsState()
    var isLoading by remember { mutableStateOf(true) }

    // Initialize task data when the screen is first launched
    LaunchedEffect(Unit) {
        taskViewModel.findAllTasks()
    }

    // Update loading state when task list changes
    LaunchedEffect(taskListState) {
        if (taskListState.isNotEmpty()) {
            isLoading = false
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, taskViewModel = taskViewModel)
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .semantics { contentDescription = "Loading tasks" },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            MainLayout(paddingValues = paddingValues) {
                NavGraph(
                    navController = navController,
                    taskViewModel = taskViewModel,
                    paddingValues = PaddingValues(0.dp) // MainLayout maneja el padding
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TaskAppTheme {
        Scaffold { paddingValues ->
            MainLayout(paddingValues = paddingValues) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Task List Content Preview",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}