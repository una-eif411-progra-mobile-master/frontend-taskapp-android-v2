package edu.mike.frontend.taskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import edu.mike.frontend.taskapp.data.datasource.TaskDataSourceImpl
import edu.mike.frontend.taskapp.data.mapper.PriorityMapper
import edu.mike.frontend.taskapp.data.mapper.StatusMapper
import edu.mike.frontend.taskapp.data.mapper.TaskMapper
import edu.mike.frontend.taskapp.data.repository.TaskRepositoryImpl
import edu.mike.frontend.taskapp.presentation.factory.TaskViewModelFactory
import edu.mike.frontend.taskapp.presentation.navigation.NavGraph
import edu.mike.frontend.taskapp.presentation.ui.layout.MainLayout
import edu.mike.frontend.taskapp.presentation.ui.theme.TaskAppTheme
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel

/**
 * Main activity that serves as the entry point for the application.
 * Initializes the TaskViewModel and sets up the Compose UI with the main screen.
 */
class MainActivity : ComponentActivity() {
    /**
     * ViewModel instance that manages task-related data and business logic.
     * Initialized using ViewModelFactory pattern to inject dependencies.
     */
    private val taskViewModel: TaskViewModel by viewModels {
        // Create mappers
        val priorityMapper = PriorityMapper()
        val statusMapper = StatusMapper()
        val taskMapper = TaskMapper(priorityMapper, statusMapper)

        // Create data source with mapper
        val dataSource = TaskDataSourceImpl(taskMapper)

        // Create repository with data source and mapper
        val taskRepository = TaskRepositoryImpl(dataSource, taskMapper)

        TaskViewModelFactory(taskRepository)
    }

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

    Scaffold { paddingValues ->
        if (isLoading) {
            // Show loading indicator when tasks are being fetched
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .semantics { contentDescription = "Loading tasks" },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Display main content when tasks are loaded
            MainLayout(paddingValues = paddingValues) {
                NavGraph(
                    navController = navController,
                    taskViewModel = taskViewModel,
                    paddingValues = PaddingValues(0.dp) // MainLayout handles padding
                )
            }
        }
    }
}

/**
 * Preview function for the MainScreen composable.
 * Provides a design-time preview of how the application will appear,
 * using sample content instead of the actual NavGraph.
 */
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TaskAppTheme {
        Scaffold { paddingValues ->
            MainLayout(paddingValues = paddingValues) {
                // Mock content to simulate NavGraph content
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