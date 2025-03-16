package edu.mike.frontend.taskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import edu.mike.frontend.taskapp.data.datasource.TaskDataSourceImpl
import edu.mike.frontend.taskapp.data.mapper.PriorityMapper
import edu.mike.frontend.taskapp.data.mapper.StatusMapper
import edu.mike.frontend.taskapp.data.mapper.TaskMapper
import edu.mike.frontend.taskapp.data.repository.TaskRepositoryImpl
import edu.mike.frontend.taskapp.presentation.factory.TaskViewModelFactory
import edu.mike.frontend.taskapp.presentation.navigation.NavGraph
import edu.mike.frontend.taskapp.presentation.ui.theme.TaskAppTheme
import edu.mike.frontend.taskapp.presentation.viewmodel.TaskViewModel

/**
 * Main activity that serves as the entry point for the application.
 * Initializes the TaskViewModel and sets up the Compose UI with the main screen.
 */
class MainActivity : ComponentActivity() {
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
 * Main screen composable that incorporates the top app bar and navigation system.
 * Sets up the application scaffold and initializes the navigation graph.
 *
 * @param taskViewModel The ViewModel that provides access to task data and business logic
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(taskViewModel: TaskViewModel) {
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        taskViewModel.findAllTasks()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }, colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }) { paddingValues ->
        NavGraph(
            navController = navController,
            taskViewModel = taskViewModel,
            paddingValues = paddingValues
        )
    }
}

/**
 * Preview composable for the MainScreen.
 * Displays the application UI with the main navigation structure.
 */
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TaskAppTheme {
        // Create a minimal MainScreen preview that doesn't require a real ViewModel
        PreviewMainScreen()
    }
}

/**
 * A simplified version of MainScreen for preview purposes only.
 * Uses static content instead of requiring a real ViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PreviewMainScreen() {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        // Display placeholder content instead of the actual NavGraph
        // which requires a real ViewModel
        Text(
            text = "Preview: Navigation Content",
            modifier = Modifier.padding(paddingValues)
        )
    }
}